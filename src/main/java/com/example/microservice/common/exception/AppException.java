package com.example.microservice.common.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppException extends RuntimeException {

    private ErrorCode errorCode;
    private int httpStatus;
    private String message;

    public AppException(ErrorCode errorCode, int httpStatus, String message) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public AppException(ErrorCode errorCode, int httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public AppException(String message) {
        super(message);
        this.errorCode = ErrorCode.INTERNAL_ERROR;
        this.httpStatus = 500;
        this.message = message;
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = ErrorCode.INTERNAL_ERROR;
        this.httpStatus = 500;
        this.message = message;
    }
}
