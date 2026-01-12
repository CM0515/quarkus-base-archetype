package com.example.microservice.domain.user.exception;

public class UserNotFoundException extends  DomainException {

    public UserNotFoundException(Long id) {
        super(
                "USER_NOT_FOUND",
                "User not found with id: " + id
        );
    }
}