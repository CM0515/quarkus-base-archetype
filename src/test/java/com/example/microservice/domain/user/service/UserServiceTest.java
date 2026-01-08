package com.example.microservice.domain.user.service;

import com.example.microservice.common.exception.AppException;
import com.example.microservice.domain.user.dto.CreateUserRequest;
import com.example.microservice.domain.user.entity.User;
import com.example.microservice.domain.user.repository.UserRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@QuarkusTest
class UserServiceTest {

    @Inject
    UserService userService;

    @InjectMock
    UserRepository userRepository;

    private CreateUserRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new CreateUserRequest(
                "John Doe",
                "john@example.com",
                "1234567890",
                "123 Main St",
                User.UserRole.USER
        );
    }

    @Test
    void testCreateUserSuccess() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        // When
        User result = userService.createUser(validRequest);

        // Then
        assertNotNull(result);
        assertEquals("john@example.com", result.getEmail());
        verify(userRepository, times(1)).existsByEmail(anyString());
    }

    @Test
    void testCreateUserWithDuplicateEmail() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        assertThrows(AppException.class, () -> userService.createUser(validRequest));
        verify(userRepository, times(1)).existsByEmail(anyString());
    }

    @Test
    void testGetUserByEmailSuccess() {
        // Given
        User user = new User(
                "John Doe",
                "john@example.com",
                null,
                null,
                User.UserRole.USER
        );
        user.id = 1L;

        when(userRepository.findByEmailActive(anyString()))
                .thenReturn(Optional.of(user));

        // When
        User result = userService.getUserByEmail("john@example.com");

        // Then
        assertNotNull(result);
        assertEquals("john@example.com", result.getEmail());
        verify(userRepository, times(1)).findByEmailActive(anyString());
    }

    @Test
    void testGetUserByEmailNotFound() {
        // Given
        when(userRepository.findByEmailActive(anyString()))
                .thenReturn(Optional.empty());

        // When & Then
        assertThrows(AppException.class, () -> userService.getUserByEmail("nonexistent@example.com"));
        verify(userRepository, times(1)).findByEmailActive(anyString());
    }
}
