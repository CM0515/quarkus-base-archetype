package com.example.microservice.domain.client.application.usecase.update;

import com.example.microservice.domain.client.domain.model.ClientRole;

import java.util.Optional;

public record UpdateClientCommand(
        Optional<String> name,
        Optional<String> email,
        Optional<String> phone,
        Optional<String> address,
        Optional<ClientRole> role
) {
}
