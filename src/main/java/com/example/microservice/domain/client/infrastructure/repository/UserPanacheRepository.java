package com.example.microservice.domain.client.infrastructure.repository;

import com.example.microservice.domain.client.infrastructure.entity.ClientEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserPanacheRepository
        implements PanacheRepository<ClientEntity> {
}
