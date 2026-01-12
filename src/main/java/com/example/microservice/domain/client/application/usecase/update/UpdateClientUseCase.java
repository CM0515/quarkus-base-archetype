package com.example.microservice.domain.client.application.usecase.update;

import com.example.microservice.domain.client.application.dto.ClientResponse;
import com.example.microservice.domain.client.domain.exception.ClientNotFountException;
import com.example.microservice.domain.client.domain.model.Client;
import com.example.microservice.domain.client.domain.port.out.ClientRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.regex.Pattern;

@ApplicationScoped
public class UpdateClientUseCase {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private final ClientRepository repository;

    public UpdateClientUseCase(ClientRepository repository){
        this.repository = repository;
    }


    @Transactional
    public ClientResponse execute(UpdateClientCommand command, Long id) {
        Client clientExists = repository.findByid(id)
                .orElseThrow(() -> new ClientNotFountException(id + ""));

        command.email().ifPresent(email -> {
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                throw new IllegalArgumentException("Invalid email format: " + email);
            }
        });

        Client updatedClient = new Client(
                command.name().orElse(clientExists.name()),
                command.email().orElse(clientExists.email()),
                command.phone().orElse(clientExists.phone()),
                command.address().orElse(clientExists.address()),
                command.role().orElse(clientExists.role())
        );

        Client client = repository.update(updatedClient, id);
        return ClientResponse.from(client);
    }
}
