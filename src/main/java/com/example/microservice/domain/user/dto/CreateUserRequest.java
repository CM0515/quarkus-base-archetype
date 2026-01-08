package com.example.microservice.domain.user.dto;

import com.example.microservice.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
    @NotBlank(message = "El nombre es requerido")
    @JsonProperty("name")
    String name,

    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe ser válido")
    @JsonProperty("email")
    String email,

    @JsonProperty("phone")
    String phone,

    @JsonProperty("address")
    String address,

    @JsonProperty("role")
    User.UserRole role
) {

    public CreateUserRequest {
        // Valor por defecto para role si es null
        if (role == null) {
            role = User.UserRole.USER;
        }
    }

    public User toEntity() {
        return new User(this.name, this.email, this.phone, this.address, this.role);
    }
}
