package com.example.microservice.domain.client.domain.exception;

public class ClientNotFountException extends DomainException {

    public ClientNotFountException(String term) {
        super(
                "CLIENT_NOY_FOUND",
                "Client not found with id: " + term
        );
    }
}