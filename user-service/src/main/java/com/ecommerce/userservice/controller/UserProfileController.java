package com.ecommerce.userservice.controller;

import com.ecommerce.userservice.dto.UserProfileDTO;
import com.ecommerce.userservice.security.JwtTokenProvider;
import com.ecommerce.userservice.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserProfileController {
    private final UserProfileService userProfileService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userProfileService.getUserProfile(userId));
    }

    @PostMapping
    public ResponseEntity<UserProfileDTO> createUserProfile(@RequestBody UserProfileDTO userProfileDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userProfileService.createUserProfile(userProfileDTO));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserProfileDTO> updateUserProfile(
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody UserProfileDTO userProfileDTO) {

        // Verify JWT if provided
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring("Bearer ".length());
            if (!jwtTokenProvider.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }

        return ResponseEntity.ok(userProfileService.updateUserProfile(userId, userProfileDTO));
    }
}

