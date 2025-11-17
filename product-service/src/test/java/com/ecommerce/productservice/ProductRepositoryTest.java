package com.ecommerce.productservice;

import com.ecommerce.productservice.entity.Product;
import com.ecommerce.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Test
    void testSaveAndFindProduct() {
        Product product = new Product(null, "Test Product", "Test Description",
                new BigDecimal("99.99"), 100);
        Product saved = productRepository.save(product);

        assertNotNull(saved.getId());
        assertEquals("Test Product", saved.getName());

        var found = productRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Test Product", found.get().getName());
        assertEquals(100, found.get().getStock());
    }

    @Test
    void testUpdateProduct() {
        Product product = new Product(null, "Product1", "Desc1",
                new BigDecimal("50.00"), 50);
        Product saved = productRepository.save(product);

        saved.setStock(30);
        Product updated = productRepository.save(saved);

        assertEquals(30, updated.getStock());
    }
}

