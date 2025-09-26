package br.com.fiap.ecopark.infrastructure.api.rest;

import br.com.fiap.ecopark.domain.model.Cliente;
import br.com.fiap.ecopark.interfaces.ClienteController;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/clientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClienteRestController {

    private final ClienteController clienteController;

    @Inject
    public ClienteRestController(ClienteController clienteController) {
        this.clienteController = clienteController;
    }

    @GET
    @Path("/ola")
    public Response olaMundo(){
        return Response.ok(
                Map.of("mensagem", "Minha primeira API REST"))
                .build();
    }

    @POST
    public Response criarCliente(Cliente clienteInput) {
        try{
            Cliente cliente = this.clienteController.criarCliente(clienteInput);
            return Response.status(Response.Status.CREATED).entity(cliente).build();
        } catch (RuntimeException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
