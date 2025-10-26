package br.com.fiap.saudetodos.domain.repository;

import java.util.List;
import br.com.fiap.saudetodos.domain.exceptions.EntidadeNaoLocalizada;
import br.com.fiap.saudetodos.infrastructure.exceptions.InfraestruturaException;
import br.com.fiap.saudetodos.domain.model.Consulta;

public interface ConsultaRepository {

    Consulta salvar(Consulta consulta) throws InfraestruturaException;

    Consulta buscarPorId(long id) throws EntidadeNaoLocalizada;

    List<Consulta> buscarTodos() throws InfraestruturaException;

    void editar(Consulta consulta) throws InfraestruturaException, EntidadeNaoLocalizada;

    void finalizar(long id, long versao) throws InfraestruturaException, EntidadeNaoLocalizada;
}
