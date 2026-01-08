package com.example.microservice.domain.user.service;

import com.example.microservice.common.exception.AppException;
import com.example.microservice.common.exception.ErrorCode;
import com.example.microservice.common.service.BaseService;
import com.example.microservice.domain.user.dto.CreateUserRequest;
import com.example.microservice.domain.user.entity.User;
import com.example.microservice.domain.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
@Transactional
public class UserService extends BaseService<User, UserRepository> {

    private static final Logger log = Logger.getLogger(UserService.class);

    @Inject
    UserRepository userRepository;

    @PostConstruct
    void init() {
        this.repository = userRepository;
    }

    public User createUser(CreateUserRequest request) {
        log.debug("Creating user with email: " + request.email());

        if (userRepository.existsByEmail(request.email())) {
            throw new AppException(
                    ErrorCode.DUPLICATE_RESOURCE,
                    409,
                    "El email " + request.email() + " ya está registrado"
            );
        }

        User user = request.toEntity();
        return create(user);
    }

    public User getUserByEmail(String email) {
        log.debug("Fetching user by email: " + email);
        return userRepository.findByEmailActive(email)
                .orElseThrow(() -> new AppException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        404,
                        "Usuario no encontrado con email: " + email
                ));
    }

    public User updateUser(Long id, CreateUserRequest request) {
        log.debug("Updating user with id: " + id);

        User user = findById(id);

        // Si el email cambió, validar que no exista otro usuario con ese email
        if (!user.getEmail().equals(request.email()) &&
            userRepository.existsByEmail(request.email())) {
            throw new AppException(
                    ErrorCode.DUPLICATE_RESOURCE,
                    409,
                    "El email " + request.email() + " ya está registrado"
            );
        }

        user.setName(request.name());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        user.setAddress(request.address());
        user.setRole(request.role());

        return update(user);
    }

    public List<User> getAllActiveUsers() {
        log.debug("Fetching all active users");
        return findAllActive();
    }
}
