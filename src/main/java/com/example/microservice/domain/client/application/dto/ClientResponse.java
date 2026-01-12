package com.example.microservice.domain.client.application.dto;

import com.example.microservice.domain.client.domain.model.Client;

public record ClientResponse(
        String name,
        String email,
        String phone,
        String address,
        String role
) {
    public static ClientResponse from(Client client) {
        return new ClientResponse(
                client.name(),
                client.email(),
                client.phone(),
                client.address(),
                client.role().name()
        );
    }
}
