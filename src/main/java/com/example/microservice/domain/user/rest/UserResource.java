package com.example.microservice.domain.user.rest;

import com.example.microservice.common.rest.BaseResource;
import com.example.microservice.domain.user.dto.CreateUserRequest;
import com.example.microservice.domain.user.dto.UserResponse;
import com.example.microservice.domain.user.entity.User;
import com.example.microservice.domain.user.service.UserService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Users", description = "Operaciones CRUD de usuarios")
public class UserResource extends BaseResource {

    private static final Logger log = Logger.getLogger(UserResource.class);

    @Inject
    UserService userService;

    @GET
    @Operation(summary = "Obtener todos los usuarios activos")
    @APIResponse(
            responseCode = "200",
            description = "Lista de usuarios obtenida correctamente",
            content = @Content(
                    schema = @Schema(
                            type = SchemaType.ARRAY,
                            implementation = UserResponse.class
                    )
            )
    )
    public Response getAllUsers() {
        log.info("GET /api/users - Fetching all active users");
        List<User> users = userService.getAllActiveUsers();
        List<UserResponse> responses = users.stream()
                .map(UserResponse::fromEntity)
                .collect(Collectors.toList());
        return ok(responses);
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Obtener un usuario por ID")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Usuario encontrado",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @APIResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public Response getUserById(
            @Parameter(description = "ID del usuario")
            @PathParam("id") Long id) {
        log.info("GET /api/users/" + id + " - Fetching user by id");
        User user = userService.findById(id);
        return ok(UserResponse.fromEntity(user));
    }

    @POST
    @Operation(summary = "Crear un nuevo usuario")
    @APIResponse(
            responseCode = "201",
            description = "Usuario creado exitosamente",
            content = @Content(schema = @Schema(implementation = UserResponse.class))
    )
    public Response createUser(@Valid CreateUserRequest request) {
        log.info("POST /api/users - Creating new user with email: " + request.email());
        User user = userService.createUser(request);
        return created(UserResponse.fromEntity(user));
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Actualizar un usuario existente")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Usuario actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @APIResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public Response updateUser(
            @Parameter(description = "ID del usuario")
            @PathParam("id") Long id,
            @Valid CreateUserRequest request) {
        log.info("PUT /api/users/" + id + " - Updating user");
        User user = userService.updateUser(id, request);
        return ok(UserResponse.fromEntity(user));
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Eliminar un usuario")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Usuario eliminado correctamente"),
            @APIResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public Response deleteUser(
            @Parameter(description = "ID del usuario")
            @PathParam("id") Long id) {
        log.info("DELETE /api/users/" + id + " - Deleting user");
        userService.delete(id);
        return noContent();
    }

    @GET
    @Path("/email/{email}")
    @Operation(summary = "Obtener usuario por email")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Usuario encontrado",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @APIResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public Response getUserByEmail(
            @Parameter(description = "Email del usuario")
            @PathParam("email") String email) {
        log.info("GET /api/users/email/" + email + " - Fetching user by email");
        User user = userService.getUserByEmail(email);
        return ok(UserResponse.fromEntity(user));
    }
}
