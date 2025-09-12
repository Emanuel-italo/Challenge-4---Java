package br.com.fiap.ecopark.domain.repository;

import br.com.fiap.ecopark.domain.exceptions.EntidadeNaoLocalizada;
import br.com.fiap.ecopark.domain.model.Cliente;

import java.util.List;

public interface ClienteRepository {
    Cliente salvar(Cliente cliente);
    Cliente buscarPorCpf(String cpf) throws EntidadeNaoLocalizada;
    Cliente editar(Cliente cliente);
    List<Cliente> buscarTodos();
    void desativar(String cpf, Long versao);
    void reativar(String cpf, Long versao);
}
