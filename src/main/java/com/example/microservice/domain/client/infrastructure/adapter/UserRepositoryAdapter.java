package com.example.microservice.domain.client.infrastructure.adapter;

import com.example.microservice.domain.client.domain.model.Client;
import com.example.microservice.domain.client.domain.model.ClientRole;
import com.example.microservice.domain.client.domain.port.out.ClientRepository;
import com.example.microservice.domain.client.infrastructure.entity.ClientEntity;
import com.example.microservice.domain.client.infrastructure.repository.UserPanacheRepository;
import com.example.microservice.domain.user.entity.User;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserRepositoryAdapter implements ClientRepository {

    private final UserPanacheRepository panache;

    public UserRepositoryAdapter(UserPanacheRepository panache) {
        this.panache = panache;
    }

    @Override
    public boolean existsByEmail(String email) {
        return false;
    }

    @Override
    public Optional<Client> findByid(Long id) {
        return panache.findByIdOptional(id).map(this::toDomain);
    }

    @Override
    public Client save(Client client) {
        ClientEntity entity = toEntity(client);
        entity.persist();
        return toDomain(entity);
    }

    @Override
    public List<Client> getAllActiveUsers() {
        return panache.listAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Client update(Client client, Long id) {
        ClientEntity entity = ClientEntity.findById(id);
        entity.name = client.name();
        entity.email = client.email();
        entity.phone = client.phone();
        entity.address = client.address();
        entity.role = client.role();
        return toDomain(entity);


    }


    private ClientEntity toEntity(Client client) {
        ClientEntity entity = new ClientEntity();
        entity.name = client.name();
        entity.email = client.email();
        entity.phone = client.phone();
        entity.address = client.address();
        entity.role = ClientRole.valueOf(client.role().name());
        return entity;
    }

    private Client toDomain(ClientEntity entity) {
        return new Client(
                entity.name,
                entity.email,
                entity.phone,
                entity.address,
                ClientRole.valueOf(entity.role.name())
        );
    }
}