package com.ecommerce.orderservice.client;

import com.ecommerce.orderservice.dto.ProductDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductClient {
    private final RestTemplate restTemplate;
    private static final String PRODUCT_SERVICE_URL = "http://localhost:8082/api/products";

    @CircuitBreaker(name = "product-service", fallbackMethod = "getProductFallback")
    public ProductDTO getProduct(Long productId) {
        try {
            String url = PRODUCT_SERVICE_URL + "/" + productId;
            return restTemplate.getForObject(url, ProductDTO.class);
        } catch (RestClientException e) {
            log.error("Error fetching product {}", productId, e);
            throw e;
        }
    }

    @CircuitBreaker(name = "product-service", fallbackMethod = "updateStockFallback")
    public void updateStock(Long productId, Integer quantity) {
        try {
            String url = PRODUCT_SERVICE_URL + "/" + productId + "/stock?quantity=" + quantity;
            restTemplate.put(url, null);
        } catch (RestClientException e) {
            log.error("Error updating stock for product {}", productId, e);
            throw e;
        }
    }

    public ProductDTO getProductFallback(Long productId, Exception e) {
        log.warn("Circuit breaker opened for getProduct {}", productId);
        // Return null or default product
        return null;
    }

    public void updateStockFallback(Long productId, Integer quantity, Exception e) {
        log.warn("Circuit breaker opened for updateStock {} - quantity: {}", productId, quantity);
        throw new RuntimeException("Product service unavailable. Unable to update stock.");
    }
}

