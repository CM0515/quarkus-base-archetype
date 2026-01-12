package com.example.microservice.domain.client.infrastructure.rest;

import com.example.microservice.domain.client.application.dto.ClientResponse;
import com.example.microservice.domain.client.application.dto.CreateClientCommand;
import com.example.microservice.domain.client.application.usecase.create.CreateClientUseCase;
import com.example.microservice.domain.client.application.usecase.findAll.GetAllClientsUsecase;
import com.example.microservice.domain.client.application.usecase.update.UpdateClientCommand;
import com.example.microservice.domain.client.application.usecase.update.UpdateClientUseCase;
import com.example.microservice.domain.client.domain.model.Client;
import com.example.microservice.domain.client.infrastructure.rest.dto.ClientRequestDTO;
import com.example.microservice.domain.client.infrastructure.rest.dto.UpdateClientRequestDTO;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/clients")
@Produces(MediaType.APPLICATION_JSON)
public class ClientResource {
    private final GetAllClientsUsecase getAllClientsUsecase;
    private final CreateClientUseCase createClientUseCase;
    private final UpdateClientUseCase updateClientUseCase;
    private final ClientMapper mapper;

    public ClientResource(
            GetAllClientsUsecase getAllClientsUsecase,
            CreateClientUseCase createClientUseCase,
            UpdateClientUseCase updateClientUseCase,
            ClientMapper mapper
    ) {
        this.getAllClientsUsecase = getAllClientsUsecase;
        this.createClientUseCase = createClientUseCase;
        this.updateClientUseCase = updateClientUseCase;
        this.mapper = mapper;
    }


    @GET
    public Response getAll() {
        List<Client> clients = getAllClientsUsecase.execute();
        List<ClientResponse> response = clients.stream()
                .map(mapper::toResponse)
                .toList();

        return Response.ok(response).build();
    }

    @POST()
    public Response create(@Valid ClientRequestDTO request) {
        CreateClientCommand command = new CreateClientCommand(
                request.name(),
                request.email(),
                request.phone(),
                request.address(),
                request.role()
        );
        ClientResponse response = createClientUseCase.execute(command);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PUT
    @Path("/{id}")
    public Response run(
            @PathParam("id") Long id,
            @Valid UpdateClientRequestDTO request
    ){
        UpdateClientCommand command = new UpdateClientCommand(
                request.name(),
                request.email(),
                request.phone(),
                request.address(),
                request.role()
        );

        return Response.ok(updateClientUseCase.execute(command,id)).build();
    }
}
