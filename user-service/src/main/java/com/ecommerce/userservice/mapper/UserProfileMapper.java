package com.ecommerce.userservice.mapper;

import com.ecommerce.userservice.dto.UserProfileDTO;
import com.ecommerce.userservice.entity.UserProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfileDTO toDTO(UserProfile userProfile);
    UserProfile toEntity(UserProfileDTO userProfileDTO);
}

