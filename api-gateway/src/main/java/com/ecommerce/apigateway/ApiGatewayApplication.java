package com.ecommerce.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-route", r -> r
                        .path("/auth/**")
                        .filters(f -> f.rewritePath("/auth/(?<segment>.*)", "/${segment}"))
                        .uri("http://localhost:8081"))
                .route("product-route", r -> r
                        .path("/products/**")
                        .filters(f -> f.rewritePath("/products/(?<segment>.*)", "/${segment}"))
                        .uri("http://localhost:8082"))
                .route("order-route", r -> r
                        .path("/orders/**")
                        .filters(f -> f.rewritePath("/orders/(?<segment>.*)", "/${segment}"))
                        .uri("http://localhost:8083"))
                .route("user-route", r -> r
                        .path("/users/**")
                        .filters(f -> f.rewritePath("/users/(?<segment>.*)", "/${segment}"))
                        .uri("http://localhost:8084"))
                .build();
    }
}

