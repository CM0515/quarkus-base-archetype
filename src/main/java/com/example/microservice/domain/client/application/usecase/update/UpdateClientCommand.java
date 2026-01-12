package com.example.microservice.domain.client.application.usecase.update;

import com.example.microservice.domain.client.domain.model.ClientRole;

public record UpdateClientCommand(
        String name,
        String email,
        String phone,
        String address,
        ClientRole role
) {
}
