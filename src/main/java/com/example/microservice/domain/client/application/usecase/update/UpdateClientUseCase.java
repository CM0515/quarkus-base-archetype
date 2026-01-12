package com.example.microservice.domain.client.application.usecase.update;

import com.example.microservice.domain.client.application.dto.ClientResponse;
import com.example.microservice.domain.client.domain.exception.ClientNotFountException;
import com.example.microservice.domain.client.domain.model.Client;
import com.example.microservice.domain.client.domain.port.out.ClientRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UpdateClientUseCase {
    private final ClientRepository repository;

    public UpdateClientUseCase(ClientRepository repository){
        this.repository = repository;
    }


    @Transactional
    public ClientResponse execute(UpdateClientCommand command, Long id) {
        Client clientExists = repository.findByid(id) .orElseThrow(() -> new ClientNotFountException(id+""));

        clientExists.update(
                command.name(),
                command.email(),
                command.phone(),
                command.address(),
                command.role()
        );

        Client client = repository.update(clientExists, id);
       return ClientResponse.from(client);

    }
}
