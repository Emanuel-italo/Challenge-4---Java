package br.com.fiap.saudetodos.infrastructure.web.resource;

import br.com.fiap.saudetodos.domain.exceptions.EntidadeNaoLocalizada;
import br.com.fiap.saudetodos.domain.model.Paciente;
import br.com.fiap.saudetodos.domain.repository.PacienteRepository; // Injetar Repo diretamente

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Path("/pacientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PacienteResource {

    @Inject
    PacienteRepository pacienteRepository;

    @POST
    public Response criarPaciente(Paciente paciente) {

        if (paciente == null || paciente.getNome() == null || paciente.getCpf() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("erro", "Dados incompletos para criar paciente."))
                    .build();
        }

        try {

            pacienteRepository.buscarPorCpf(paciente.getCpf());

            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of("erro", "CPF já cadastrado."))
                    .build();
        } catch (EntidadeNaoLocalizada e) {

            Paciente pacienteSalvo = pacienteRepository.salvar(paciente);
            if (pacienteSalvo != null) {

                return Response.status(Response.Status.CREATED).entity(pacienteSalvo).build();
            } else {

                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(Map.of("erro", "Erro interno ao salvar paciente."))
                        .build();
            }
        } catch (Exception e) {

            System.err.println("Erro inesperado ao verificar CPF: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("erro", "Erro interno ao verificar CPF."))
                    .build();
        }
    }

    @GET
    public Response listarPacientes() {
        try {
            List<Paciente> pacientes = pacienteRepository.buscarTodos();
            return Response.ok(pacientes).build();
        } catch (Exception e) {
            System.err.println("Erro ao buscar todos os pacientes: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("erro", "Erro interno ao buscar pacientes."))
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarPacientePorId(@PathParam("id") int id) {
        try {
            Paciente paciente = pacienteRepository.buscarPorId(id);
            return Response.ok(paciente).build();
        } catch (EntidadeNaoLocalizada e) {

            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("erro", e.getMessage()))
                    .build();
        } catch (Exception e) {
            System.err.println("Erro ao buscar paciente por ID: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("erro", "Erro interno ao buscar paciente por ID."))
                    .build();
        }
    }

    @GET
    @Path("/cpf/{cpf}")
    public Response buscarPacientePorCpf(@PathParam("cpf") String cpf) {
        try {
            Paciente paciente = pacienteRepository.buscarPorCpf(cpf);
            return Response.ok(paciente).build();
        } catch (EntidadeNaoLocalizada e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("erro", e.getMessage()))
                    .build();
        } catch (Exception e) {
            System.err.println("Erro ao buscar paciente por CPF: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("erro", "Erro interno ao buscar paciente por CPF."))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response atualizarPaciente(@PathParam("id") int id, Paciente paciente) {
        // Validação básica
        if (paciente == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("erro", "Corpo da requisição vazio.")).build();
        }


        paciente.setId(id);

        try {

            pacienteRepository.buscarPorId(id);

            boolean sucesso = pacienteRepository.editar(paciente);
            if (sucesso) {

                Paciente pacienteAtualizado = pacienteRepository.buscarPorId(id);
                return Response.ok(pacienteAtualizado).build(); // 200 OK
            } else {

                try {
                    pacienteRepository.buscarPorId(id);

                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("erro", "Erro interno ao atualizar paciente.")).build();
                } catch (EntidadeNaoLocalizada naoAchou) {

                    return Response.status(Response.Status.NOT_FOUND).entity(Map.of("erro", "Paciente não encontrado ou inativo para atualização.")).build();
                }
            }
        } catch (EntidadeNaoLocalizada e) {

            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("erro", e.getMessage())).build();
        } catch (Exception e) {
            System.err.println("Erro ao atualizar paciente: " + e.getMessage());
            e.printStackTrace();

            if (e instanceof IllegalArgumentException) {
                return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("erro", "Dados inválidos: " + e.getMessage())).build();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("erro", "Erro interno ao atualizar paciente.")).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response desativarPaciente(@PathParam("id") int id) {
        try {
            boolean sucesso = pacienteRepository.desativar(id);
            if (sucesso) {
                return Response.noContent().build();
            } else {

                try {
                    pacienteRepository.buscarPorId(id);

                    System.err.println("Erro inesperado ao desativar paciente ID: " + id + " (estava ativo mas falhou?)");
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("erro", "Erro inesperado ao desativar.")).build();
                } catch (EntidadeNaoLocalizada e) {

                    return Response.status(Response.Status.NOT_FOUND).entity(Map.of("erro", "Paciente não encontrado ou já inativo.")).build();
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao desativar paciente: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("erro", "Erro interno ao desativar paciente.")).build();
        }
    }
}