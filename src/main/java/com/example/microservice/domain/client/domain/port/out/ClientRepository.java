package com.example.microservice.domain.client.domain.port.out;

import com.example.microservice.domain.client.domain.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {
    boolean existsByEmail(String email);
    Optional<Client> findByid(Long id);
    Client save(Client client);
    List<Client> getAllActiveUsers();
    Client update(Client client, Long id);
}
