package com.example.microservice.common.exception;

public class AppException extends RuntimeException {

    private ErrorCode errorCode;
    private int httpStatus;
    private String message;

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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
