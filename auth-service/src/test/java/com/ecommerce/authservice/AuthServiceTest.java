package com.ecommerce.authservice;

import com.ecommerce.authservice.entity.User;
import com.ecommerce.authservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AuthServiceTest {
    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void testPasswordHashingAndMatching() {
        String rawPassword = "testPassword123";
        String hashedPassword = passwordEncoder.encode(rawPassword);

        assertNotEquals(rawPassword, hashedPassword);
        assertTrue(passwordEncoder.matches(rawPassword, hashedPassword));
        assertFalse(passwordEncoder.matches("wrongPassword", hashedPassword));
    }

    @Test
    void testSaveAndFindUser() {
        User user = new User("testuser", "test@example.com",
                passwordEncoder.encode("password123"), "ROLE_USER");
        userRepository.save(user);

        var foundUser = userRepository.findByUsername("testuser");
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
        assertEquals("test@example.com", foundUser.get().getEmail());
    }
}

