package com.ecommerce.orderservice.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String ECOMMERCE_EXCHANGE = "ecommerce";
    public static final String ORDER_CREATED_QUEUE = "order.created";
    public static final String ORDER_CREATED_ROUTING_KEY = "order.created";

    @Bean
    public DirectExchange ecommerceExchange() {
        return new DirectExchange(ECOMMERCE_EXCHANGE, true, false);
    }

    @Bean
    public Queue orderCreatedQueue() {
        return new Queue(ORDER_CREATED_QUEUE, true);
    }

    @Bean
    public Binding orderCreatedBinding(Queue orderCreatedQueue, DirectExchange ecommerceExchange) {
        return BindingBuilder.bind(orderCreatedQueue)
                .to(ecommerceExchange)
                .with(ORDER_CREATED_ROUTING_KEY);
    }
}

