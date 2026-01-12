package com.example.microservice.domain.client.application.usecase.create;

import com.example.microservice.domain.client.application.dto.ClientResponse;
import com.example.microservice.domain.client.application.dto.CreateClientCommand;
import com.example.microservice.domain.client.domain.exception.DuplicateEmailException;
import com.example.microservice.domain.client.domain.model.Client;
import com.example.microservice.domain.client.domain.port.out.ClientRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CreateClientUseCase {
    private final ClientRepository repository;

    public CreateClientUseCase(ClientRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ClientResponse execute(CreateClientCommand command) {

        if (repository.existsByEmail(command.email())) {
            throw new DuplicateEmailException(command.email());
        }

        Client client = new Client(
                command.name(),
                command.email(),
                command.phone(),
                command.address(),
                command.role()
        );

        Client saved = repository.save(client);

        return ClientResponse.from(saved);
    }
}
