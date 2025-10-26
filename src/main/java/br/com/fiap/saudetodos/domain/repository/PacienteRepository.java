package br.com.fiap.saudetodos.domain.repository;

import java.util.List;
import br.com.fiap.saudetodos.domain.exceptions.EntidadeNaoLocalizada;
import br.com.fiap.saudetodos.infrastructure.exceptions.InfraestruturaException;
import br.com.fiap.saudetodos.domain.model.Paciente;

public interface PacienteRepository {

    Paciente salvar(Paciente paciente) throws InfraestruturaException;

    Paciente buscarPorId(int id) throws EntidadeNaoLocalizada;

    List<Paciente> buscarTodos() throws InfraestruturaException;

    void editar(Paciente paciente) throws InfraestruturaException, EntidadeNaoLocalizada;

    void desativar(int id, long versao) throws InfraestruturaException, EntidadeNaoLocalizada;

    void reativar(int id, long versao) throws InfraestruturaException, EntidadeNaoLocalizada;
}
