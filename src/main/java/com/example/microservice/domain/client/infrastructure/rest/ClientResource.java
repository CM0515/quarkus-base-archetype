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
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
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
    public Multi<ClientResponse> getAll() {
        return Uni.createFrom().item(getAllClientsUsecase::execute)
                .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
                .onItem().transformToMulti(clients -> Multi.createFrom().iterable(clients))
                .map(mapper::toResponse);
    }

    @POST()
    public Uni<Response> create(@Valid ClientRequestDTO request) {
        return Uni.createFrom().item(() -> {
            CreateClientCommand command = new CreateClientCommand(
                    request.name(),
                    request.email(),
                    request.phone(),
                    request.address(),
                    request.role()
            );
            ClientResponse response = createClientUseCase.execute(command);
            return Response.status(Response.Status.CREATED).entity(response).build();
        }).runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
    }

    @PUT
    @Path("/{id}")
    public Uni<Response> run(
            @PathParam("id") Long id,
            @Valid UpdateClientRequestDTO request
    ){
        return Uni.createFrom().item(() -> {
            UpdateClientCommand command = new UpdateClientCommand(
                    request.name(),
                    request.email(),
                    request.phone(),
                    request.address(),
                    request.role()
            );

            return Response.ok(updateClientUseCase.execute(command,id)).build();
        }).runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
    }
}
