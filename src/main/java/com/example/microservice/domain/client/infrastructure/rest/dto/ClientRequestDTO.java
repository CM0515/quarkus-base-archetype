package com.example.microservice.domain.client.infrastructure.rest.dto;

import com.example.microservice.domain.client.domain.model.ClientRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClientRequestDTO(
        @NotBlank String name,
        @NotBlank @Email String email,
        String phone,
        String address,
        ClientRole role

) {
}
