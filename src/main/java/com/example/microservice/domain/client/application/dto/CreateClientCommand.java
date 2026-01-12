package com.example.microservice.domain.client.application.dto;

import com.example.microservice.domain.client.domain.model.Client;
import com.example.microservice.domain.client.domain.model.ClientRole;

public record CreateClientCommand(
        String name,
        String email,
        String phone,
        String address,
        ClientRole role
) {
}
