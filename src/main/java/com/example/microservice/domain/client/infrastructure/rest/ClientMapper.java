package com.example.microservice.domain.client.infrastructure.rest;

import com.example.microservice.domain.client.application.dto.ClientResponse;
import com.example.microservice.domain.client.domain.model.Client;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ClientMapper {
    public ClientResponse toResponse(Client client) {
        return new ClientResponse(
                client.name(),
                client.email(),
                client.phone(),
                client.address(),
                client.role().name()
        );
    }
}
