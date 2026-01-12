package com.example.microservice.domain.client.application.usecase.findAll;

import com.example.microservice.domain.client.domain.model.Client;
import com.example.microservice.domain.client.domain.port.out.ClientRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

 @ApplicationScoped
public class GetAllClientsUsecase {
    private final ClientRepository repository;

    public GetAllClientsUsecase(ClientRepository repository) {
        this.repository = repository;
    }

    public List<Client> execute(){
        return  this.repository.getAllActiveUsers();
    }
}
