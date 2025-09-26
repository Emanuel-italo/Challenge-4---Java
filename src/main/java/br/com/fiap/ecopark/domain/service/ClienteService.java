package br.com.fiap.ecopark.domain.service;

import br.com.fiap.ecopark.domain.model.Cliente;

public interface ClienteService {

    Cliente criar(Cliente cliente);
    void desativar(Long id, Long version);
}
