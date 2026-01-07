package com.example.microservice.domain.user.dto;

import com.example.microservice.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {

    @NotBlank(message = "El nombre es requerido")
    @JsonProperty("name")
    private String name;

    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe ser válido")
    @JsonProperty("email")
    private String email;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("address")
    private String address;

    @JsonProperty("role")
    @Builder.Default
    private User.UserRole role = User.UserRole.USER;

    public User toEntity() {
        return User.builder()
                .name(this.name)
                .email(this.email)
                .phone(this.phone)
                .address(this.address)
                .role(this.role)
                .build();
    }
}
