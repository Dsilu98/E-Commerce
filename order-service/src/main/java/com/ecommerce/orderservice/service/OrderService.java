package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.client.ProductClient;
import com.ecommerce.orderservice.config.RabbitMQConfig;
import com.ecommerce.orderservice.dto.CreateOrderRequest;
import com.ecommerce.orderservice.dto.OrderDTO;
import com.ecommerce.orderservice.dto.OrderItemDTO;
import com.ecommerce.orderservice.dto.ProductDTO;
import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.event.OrderCreatedEvent;
import com.ecommerce.orderservice.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public OrderDTO createOrder(Long userId, CreateOrderRequest request) {
        BigDecimal totalAmount = BigDecimal.ZERO;

        // Validate and fetch product details
        for (OrderItemDTO item : request.getItems()) {
            ProductDTO product = productClient.getProduct(item.getProductId());
            if (product == null) {
                throw new IllegalArgumentException("Product not found: " + item.getProductId());
            }
            if (product.getStock() < item.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product: " + item.getProductId());
            }
            totalAmount = totalAmount.add(product.getPrice().multiply(new BigDecimal(item.getQuantity())));
        }

        // Create order
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setStatus("CONFIRMED");
        try {
            order.setItems(objectMapper.writeValueAsString(request.getItems()));
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize items", e);
        }

        Order savedOrder = orderRepository.save(order);

        // Update stock for each product
        for (OrderItemDTO item : request.getItems()) {
            productClient.updateStock(item.getProductId(), item.getQuantity());
        }

        // Publish order created event
        publishOrderCreatedEvent(savedOrder, request.getItems());

        return convertToDTO(savedOrder, request.getItems());
    }

    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
        return convertToDTO(order, parseItems(order.getItems()));
    }

    public List<OrderDTO> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(order -> convertToDTO(order, parseItems(order.getItems())))
                .collect(Collectors.toList());
    }

    private void publishOrderCreatedEvent(Order order, List<OrderItemDTO> items) {
        try {
            List<OrderCreatedEvent.OrderItemEvent> eventItems = items.stream()
                    .map(item -> new OrderCreatedEvent.OrderItemEvent(item.getProductId(), item.getQuantity()))
                    .collect(Collectors.toList());

            OrderCreatedEvent event = new OrderCreatedEvent(
                    order.getId(),
                    order.getUserId(),
                    order.getTotalAmount(),
                    eventItems
            );

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ECOMMERCE_EXCHANGE,
                    RabbitMQConfig.ORDER_CREATED_ROUTING_KEY,
                    event
            );
            log.info("Order created event published: orderId={}", order.getId());
        } catch (Exception e) {
            log.error("Failed to publish order created event", e);
        }
    }

    private List<OrderItemDTO> parseItems(String itemsJson) {
        try {
            return objectMapper.readValue(itemsJson,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, OrderItemDTO.class));
        } catch (Exception e) {
            log.error("Failed to parse items", e);
            return List.of();
        }
    }

    private OrderDTO convertToDTO(Order order, List<OrderItemDTO> items) {
        return new OrderDTO(
                order.getId(),
                order.getUserId(),
                order.getTotalAmount(),
                order.getStatus(),
                items,
                order.getCreatedAt()
        );
    }
}

