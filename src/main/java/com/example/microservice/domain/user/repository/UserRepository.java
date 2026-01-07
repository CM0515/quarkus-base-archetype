package com.example.microservice.domain.user.repository;

import com.example.microservice.common.repository.BaseRepository;
import com.example.microservice.domain.user.entity.User;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class UserRepository implements BaseRepository<User> {

    /**
     * Busca un usuario por email
     */
    public Optional<User> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }

    /**
     * Busca un usuario activo por email
     */
    public Optional<User> findByEmailActive(String email) {
        return find("email = ?1 and isActive = true", email).firstResultOptional();
    }

    /**
     * Verifica si un email ya existe
     */
    public boolean existsByEmail(String email) {
        return find("email", email).firstResultOptional().isPresent();
    }
}
