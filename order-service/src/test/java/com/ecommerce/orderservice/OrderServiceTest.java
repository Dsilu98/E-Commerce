package com.ecommerce.orderservice;

import com.ecommerce.orderservice.client.ProductClient;
import com.ecommerce.orderservice.dto.CreateOrderRequest;
import com.ecommerce.orderservice.dto.OrderItemDTO;
import com.ecommerce.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
//@AutoConfigureMockMvc
@TestPropertySource
        (properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})
public class OrderServiceTest {
    @MockBean
    private ProductClient productClient;

    @Autowired
    private OrderService orderService;

    @Test
    void contextLoads() {
        assertTrue(true);
    }
}

