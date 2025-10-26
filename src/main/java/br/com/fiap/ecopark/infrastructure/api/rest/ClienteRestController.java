package br.com.fiap.ecopark.infrastructure.api.rest;

import br.com.fiap.ecopark.domain.exceptions.EntidadeNaoLocalizada;
import br.com.fiap.ecopark.domain.model.Cliente;
import br.com.fiap.ecopark.interfaces.ClienteController;
import br.com.fiap.ecopark.interfaces.dto.output.ClienteOutputDto;
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

//DATA TRANSFER OBJECT
    @GET
    @Path("/{cpf}")
    public Response buscarPorCpf(@PathParam("cpf") String cpf) {
        try {
            ClienteOutputDto cliente = this.clienteController.buscarById(cpf);
            return Response.ok(cliente).build();
        } catch (RuntimeException | EntidadeNaoLocalizada e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    public Response criarCliente(Cliente clienteInput) {
        try {
            Cliente cliente = this.clienteController.criarCliente(clienteInput);
            return Response.status(Response.Status.CREATED).entity(cliente).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PUT
    public Response atualizarCliente(Cliente clienteInput) {
        try {
            Cliente cliente = this.clienteController.atualizarCliente(clienteInput);
            return Response.status(Response.Status.ACCEPTED).entity(cliente).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{cpf}-{versao}")
    public Response delete(@PathParam("cpf") String cpf, @PathParam("versao") Integer versao) {
        try {
            this.clienteController.delete(cpf, versao);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }


    @GET
    @Path("/meu-nome/{name}")
    public Response testeName(@PathParam("name") String name) {
        return Response.status(Response.Status.OK).entity(Map.of("mensagem", "Bem-vindo(a) " + name)).build();
    }

}
