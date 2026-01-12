package com.example.microservice.domain.client.infrastructure.rest.dto;

import com.example.microservice.domain.client.domain.model.ClientRole;

import java.util.Optional;

public record UpdateClientRequestDTO(
        Optional<String> name,
        Optional<String> email,
        Optional<String> phone,
        Optional<String> address,
        Optional<ClientRole> role

) {
}
