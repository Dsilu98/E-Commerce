package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.UserProfileDTO;
import com.ecommerce.userservice.entity.UserProfile;
import com.ecommerce.userservice.mapper.UserProfileMapper;
import com.ecommerce.userservice.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    public UserProfileDTO getUserProfile(Long userId) {
        return userProfileRepository.findByUserId(userId)
                .map(userProfileMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("User profile not found: " + userId));
    }

    public UserProfileDTO createUserProfile(UserProfileDTO userProfileDTO) {
        UserProfile userProfile = userProfileMapper.toEntity(userProfileDTO);
        UserProfile saved = userProfileRepository.save(userProfile);
//        System.out.println("Creating user profile for userId: " + saved.getUserId());
        return userProfileMapper.toDTO(saved);
    }

    public UserProfileDTO updateUserProfile(Long userId, UserProfileDTO userProfileDTO) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User profile not found: " + userId));

        if (userProfileDTO.getEmail() != null) userProfile.setEmail(userProfileDTO.getEmail());
        if (userProfileDTO.getAddress() != null) userProfile.setAddress(userProfileDTO.getAddress());
        if (userProfileDTO.getPhone() != null) userProfile.setPhone(userProfileDTO.getPhone());

        UserProfile updated = userProfileRepository.save(userProfile);
        return userProfileMapper.toDTO(updated);
    }
}

