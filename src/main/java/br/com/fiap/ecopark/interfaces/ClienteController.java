package br.com.fiap.ecopark.interfaces;

import br.com.fiap.ecopark.domain.exceptions.EntidadeNaoLocalizada;
import br.com.fiap.ecopark.domain.model.Cliente;

public interface ClienteController {

    Cliente criarCliente(Cliente clienteInput);

    Cliente atualizarCliente(Cliente clienteInput);

    void delete(String cpf, Integer versao);

    Cliente buscarById(String cpf) throws EntidadeNaoLocalizada;
}
