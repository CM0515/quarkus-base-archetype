package com.example.microservice.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    
    RESOURCE_NOT_FOUND("ERR_001", "Recurso no encontrado", 404),
    VALIDATION_ERROR("ERR_002", "Error de validación", 400),
    UNAUTHORIZED("ERR_003", "No autorizado", 401),
    FORBIDDEN("ERR_004", "Acceso prohibido", 403),
    DUPLICATE_RESOURCE("ERR_005", "Recurso duplicado", 409),
    INVALID_REQUEST("ERR_006", "Solicitud inválida", 400),
    DATABASE_ERROR("ERR_007", "Error en la base de datos", 500),
    INTERNAL_ERROR("ERR_008", "Error interno del servidor", 500),
    BAD_GATEWAY("ERR_009", "Error de puerta de enlace", 502),
    SERVICE_UNAVAILABLE("ERR_010", "Servicio no disponible", 503);

    private final String code;
    private final String message;
    private final int httpStatus;

    ErrorCode(String code, String message, int httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
