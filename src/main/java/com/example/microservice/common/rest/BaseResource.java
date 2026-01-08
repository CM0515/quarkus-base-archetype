package com.example.microservice.common.rest;

import com.example.microservice.common.dto.ErrorResponse;
import com.example.microservice.common.dto.SuccessResponse;
import org.jboss.logging.Logger;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.ws.rs.core.Response;

@Tag(name = "Base Resource", description = "Clase base para recursos REST")
public abstract class BaseResource {

    protected static final Logger log = Logger.getLogger(BaseResource.class);

    protected <T> Response created(T data) {
        log.debug("Resource created successfully");
        return Response.status(Response.Status.CREATED)
                .entity(SuccessResponse.of(data, "Recurso creado exitosamente"))
                .build();
    }

    protected <T> Response ok(T data) {
        log.debug("Request processed successfully");
        return Response.ok()
                .entity(SuccessResponse.of(data))
                .build();
    }

    protected <T> Response ok(T data, String message) {
        log.debug("Request processed successfully");
        return Response.ok()
                .entity(SuccessResponse.of(data, message))
                .build();
    }

    protected Response accepted() {
        log.debug("Request accepted");
        return Response.accepted()
                .entity(SuccessResponse.of(null, "Solicitud aceptada"))
                .build();
    }

    protected Response noContent() {
        log.debug("No content");
        return Response.noContent().build();
    }

    protected Response notFound(String message) {
        log.warn("Resource not found: " + message);
        return Response.status(Response.Status.NOT_FOUND)
                .entity(ErrorResponse.of("ERR_001", message, null))
                .build();
    }

    protected Response badRequest(String message) {
        log.warn("Bad request: " + message);
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(ErrorResponse.of("ERR_006", message, null))
                .build();
    }

    protected Response error(int status, String code, String message) {
        log.error("Error " + status + ": " + message);
        return Response.status(status)
                .entity(ErrorResponse.of(code, message, null))
                .build();
    }
}
