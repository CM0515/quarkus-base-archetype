package com.example.microservice.common.rest.advice;

import com.example.microservice.common.domain.exception.DomainException;
import com.example.microservice.common.dto.ErrorResponse;
import com.example.microservice.common.exception.AppException;
import com.example.microservice.common.exception.ErrorCode;
import com.example.microservice.user.domain.exception.DuplicateEmailException;
import com.example.microservice.user.domain.exception.UserNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

@Provider
@Slf4j
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(Throwable exception) {
        log.error("Exception occurred: ", exception);

        // Hexagonal Architecture - Domain Exceptions
        if (exception instanceof DuplicateEmailException) {
            return handleDuplicateEmail((DuplicateEmailException) exception);
        } else if (exception instanceof UserNotFoundException) {
            return handleUserNotFound((UserNotFoundException) exception);
        } else if (exception instanceof IllegalArgumentException) {
            return handleValidationError(exception);
        } else if (exception instanceof DomainException) {
            return handleDomainException((DomainException) exception);
        }

        // Legacy exceptions
        if (exception instanceof AppException) {
            return handleAppException((AppException) exception);
        } else if (exception instanceof ConstraintViolationException) {
            return handleValidationException((ConstraintViolationException) exception);
        }

        return handleGenericException(exception);
    }

    private Response handleAppException(AppException ex) {
        ErrorResponse errorResponse = ErrorResponse.of(
                ex.getErrorCode().getCode(),
                ex.getMessage(),
                ex.getMessage(),
                getPath(),
                ex.getHttpStatus()
        );
        return Response.status(ex.getHttpStatus())
                .entity(errorResponse)
                .build();
    }

    private Response handleValidationException(ConstraintViolationException ex) {
        String details = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        ErrorResponse errorResponse = ErrorResponse.of(
                ErrorCode.VALIDATION_ERROR.getCode(),
                ErrorCode.VALIDATION_ERROR.getMessage(),
                details,
                getPath(),
                400
        );
        return Response.status(400)
                .entity(errorResponse)
                .build();
    }

    private Response handleGenericException(Throwable exception) {
        ErrorResponse errorResponse = ErrorResponse.of(
                ErrorCode.INTERNAL_ERROR.getCode(),
                ErrorCode.INTERNAL_ERROR.getMessage(),
                exception.getMessage(),
                getPath(),
                500
        );
        return Response.status(500)
                .entity(errorResponse)
                .build();
    }

    private Response handleDuplicateEmail(DuplicateEmailException ex) {
        ErrorResponse errorResponse = ErrorResponse.of(
                ex.getErrorCode(),
                ex.getMessage(),
                ex.getMessage(),
                getPath(),
                409
        );
        return Response.status(Response.Status.CONFLICT)
                .entity(errorResponse)
                .build();
    }

    private Response handleUserNotFound(UserNotFoundException ex) {
        ErrorResponse errorResponse = ErrorResponse.of(
                ex.getErrorCode(),
                ex.getMessage(),
                ex.getMessage(),
                getPath(),
                404
        );
        return Response.status(Response.Status.NOT_FOUND)
                .entity(errorResponse)
                .build();
    }

    private Response handleValidationError(Throwable ex) {
        ErrorResponse errorResponse = ErrorResponse.of(
                "VALIDATION_ERROR",
                "Error de validación",
                ex.getMessage(),
                getPath(),
                400
        );
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .build();
    }

    private Response handleDomainException(DomainException ex) {
        ErrorResponse errorResponse = ErrorResponse.of(
                ex.getErrorCode(),
                ex.getMessage(),
                ex.getMessage(),
                getPath(),
                500
        );
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorResponse)
                .build();
    }

    private String getPath() {
        try {
            return uriInfo != null ? uriInfo.getPath() : "unknown";
        } catch (Exception e) {
            return "unknown";
        }
    }
}
