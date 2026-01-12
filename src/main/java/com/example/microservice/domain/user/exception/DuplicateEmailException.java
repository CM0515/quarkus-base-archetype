package com.example.microservice.domain.user.exception;

public class DuplicateEmailException extends DomainException {

    public DuplicateEmailException(String email) {
        super(
                "USER_DUPLICATE_EMAIL",
                "Email already exists: " + email
        );
    }
}
