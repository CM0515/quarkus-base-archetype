package com.example.microservice.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
    String code,
    String message,
    String details,
    LocalDateTime timestamp,
    String path,
    int status
) {

    public static ErrorResponse of(String code, String message, String path) {
        return new ErrorResponse(
                code,
                message,
                null,
                LocalDateTime.now(),
                path,
                0
        );
    }

    public static ErrorResponse of(String code, String message, String details, String path, int status) {
        return new ErrorResponse(
                code,
                message,
                details,
                LocalDateTime.now(),
                path,
                status
        );
    }
}
