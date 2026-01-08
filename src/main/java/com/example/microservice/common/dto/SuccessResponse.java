package com.example.microservice.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SuccessResponse<T>(
    T data,
    String message,
    LocalDateTime timestamp,
    boolean success
) {

    public static <T> SuccessResponse<T> of(T data) {
        return new SuccessResponse<>(
                data,
                null,
                LocalDateTime.now(),
                true
        );
    }

    public static <T> SuccessResponse<T> of(T data, String message) {
        return new SuccessResponse<>(
                data,
                message,
                LocalDateTime.now(),
                true
        );
    }
}
