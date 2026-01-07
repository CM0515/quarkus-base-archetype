package com.example.microservice.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    
    private String code;
    private String message;
    private String details;
    private LocalDateTime timestamp;
    private String path;
    private int status;
    
    public static ErrorResponse of(String code, String message, String path) {
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    public static ErrorResponse of(String code, String message, String details, String path, int status) {
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .details(details)
                .path(path)
                .status(status)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
