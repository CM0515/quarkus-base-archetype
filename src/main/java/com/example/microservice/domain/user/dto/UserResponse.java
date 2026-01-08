package com.example.microservice.domain.user.dto;

import com.example.microservice.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record UserResponse(
    @JsonProperty("id")
    Long id,

    @JsonProperty("name")
    String name,

    @JsonProperty("email")
    String email,

    @JsonProperty("phone")
    String phone,

    @JsonProperty("address")
    String address,

    @JsonProperty("role")
    User.UserRole role,

    @JsonProperty("created_at")
    LocalDateTime createdAt,

    @JsonProperty("updated_at")
    LocalDateTime updatedAt,

    @JsonProperty("is_active")
    Boolean isActive
) {

    public static UserResponse fromEntity(User user) {
        return new UserResponse(
                user.id,
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getIsActive()
        );
    }
}
