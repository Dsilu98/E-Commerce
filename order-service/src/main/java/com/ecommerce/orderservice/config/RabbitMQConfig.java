package com.ecommerce.orderservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ECOMMERCE_EXCHANGE = "ecommerce";
    public static final String ORDER_CREATED_QUEUE = "order.created";
    public static final String ORDER_CREATED_ROUTING_KEY = "order.created";

    // ---------- Exchange / Queue / Binding (unchanged) ----------
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

    // ---------- Jackson JSON converter + type mapping ----------
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);

        // Tighten security: whitelist only your packages
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        // add packages that contain your event DTOs/entities
        typeMapper.setTrustedPackages("com.ecommerce.orderservice", "com.ecommerce.common");

        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }

    // ---------- RabbitTemplate -> use JSON converter for publishing ----------
    @Bean
    public RabbitTemplate rabbitTemplate(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }

    // ---------- Listener container factory -> use JSON converter for listeners ----------
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter converter) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(converter);

        // optional tuning (concurrency etc)
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(5);

        return factory;
    }

    // Optional: RabbitAdmin to auto-declare exchange/queue/binding at startup
    @Bean
    public RabbitAdmin rabbitAdmin(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
}
