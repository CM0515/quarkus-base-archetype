package com.example.microservice.domain.user.service;

import com.example.microservice.domain.user.application.dto.CreateUserCommand;
import com.example.microservice.domain.user.application.dto.UserResponse;
import com.example.microservice.domain.user.application.usecase.create.CreateUserUseCase;
import com.example.microservice.domain.user.application.usecase.delete.DeleteUserUseCase;
import com.example.microservice.domain.user.application.usecase.findById.FindUserByIdUseCase;
import com.example.microservice.domain.user.application.usecase.update.UpdateUserCommand;
import com.example.microservice.domain.user.application.usecase.update.UpdateUserUseCase;
import com.example.microservice.domain.user.domain.exception.DuplicateEmailException;
import com.example.microservice.domain.user.domain.exception.UserNotFoundException;
import com.example.microservice.domain.user.domain.model.User;
import com.example.microservice.domain.user.domain.model.UserRole;
import com.example.microservice.domain.user.domain.port.out.UserRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class UserServiceTest {

    @InjectMock
    UserRepository userRepository;

    @Inject
    CreateUserUseCase createUserUseCase;

    @Inject
    FindUserByIdUseCase findUserByIdUseCase;

    @Inject
    UpdateUserUseCase updateUserUseCase;

    @Inject
    DeleteUserUseCase deleteUserUseCase;

    private User mockUser;
    private CreateUserCommand createUserCommand;

    @BeforeEach
    void setup() {
        mockUser = new User(
                "Test User",
                "test@example.com",
                "1234567890",
                "123 Test St",
                UserRole.USER
        );

        createUserCommand = new CreateUserCommand(
                "Test User",
                "test@example.com",
                "1234567890",
                "123 Test St",
                UserRole.USER
        );
    }

    @Test
    void testCreateUserSuccess() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // Act
        UserResponse response = createUserUseCase.execute(createUserCommand);

        // Assert
        assertNotNull(response);
        assertEquals("Test User", response.name());
        assertEquals("test@example.com", response.email());
        assertEquals("1234567890", response.phone());
        assertEquals("123 Test St", response.address());
        assertEquals("USER", response.role());

        verify(userRepository, times(1)).existsByEmail("test@example.com");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUserWithDuplicateEmail() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateEmailException.class, () -> {
            createUserUseCase.execute(createUserCommand);
        });

        verify(userRepository, times(1)).existsByEmail("test@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testFindUserByIdSuccess() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        // Act
        User result = findUserByIdUseCase.execute(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Test User", result.name());
        assertEquals("test@example.com", result.email());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindUserByIdNotFound() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            findUserByIdUseCase.execute(999L);
        });

        verify(userRepository, times(1)).findById(999L);
    }

    @Test
    void testUpdateUserSuccess() {
        // Arrange
        UpdateUserCommand updateCommand = new UpdateUserCommand(
                Optional.of("Updated User"),
                Optional.of("updated@example.com"),
                Optional.of("0987654321"),
                Optional.of("456 Updated St"),
                Optional.of(UserRole.ADMIN)
        );

        User updatedUser = new User(
                "Updated User",
                "updated@example.com",
                "0987654321",
                "456 Updated St",
                UserRole.ADMIN
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userRepository.update(any(User.class), eq(1L))).thenReturn(updatedUser);

        // Act
        UserResponse response = updateUserUseCase.execute(updateCommand, 1L);

        // Assert
        assertNotNull(response);
        assertEquals("Updated User", response.name());
        assertEquals("updated@example.com", response.email());
        assertEquals("0987654321", response.phone());
        assertEquals("456 Updated St", response.address());
        assertEquals("ADMIN", response.role());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).update(any(User.class), eq(1L));
    }

    @Test
    void testUpdateUserNotFound() {
        // Arrange
        UpdateUserCommand updateCommand = new UpdateUserCommand(
                Optional.of("Updated User"),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            updateUserUseCase.execute(updateCommand, 999L);
        });

        verify(userRepository, times(1)).findById(999L);
        verify(userRepository, never()).update(any(User.class), anyLong());
    }

    @Test
    void testDeleteUserSuccess() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        doNothing().when(userRepository).delete(1L);

        // Act
        assertDoesNotThrow(() -> deleteUserUseCase.execute(1L));

        // Assert
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).delete(1L);
    }

    @Test
    void testDeleteUserNotFound() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            deleteUserUseCase.execute(999L);
        });

        verify(userRepository, times(1)).findById(999L);
        verify(userRepository, never()).delete(anyLong());
    }
}
