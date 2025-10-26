package br.com.fiap.ecopark.domain.service;


import br.com.fiap.ecopark.domain.exceptions.ClienteConsistenceError;
import br.com.fiap.ecopark.domain.exceptions.EntidadeNaoLocalizada;
import br.com.fiap.ecopark.domain.model.Cliente;

import java.util.List;

public interface ClienteService {
    
    Cliente criar(Cliente cliente);
   
    Cliente editar(Cliente cliente); 

    void desativar(String cpf, Long versao);
    
    void reativar(String cpf, Long versao) throws ClienteConsistenceError;
    
    Cliente localizar(String cpf) throws EntidadeNaoLocalizada;

    List<Cliente> listarTodos();
}
