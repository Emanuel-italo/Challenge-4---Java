package br.com.fiap.ecopark.interfaces;

import br.com.fiap.ecopark.domain.exceptions.EntidadeNaoLocalizada;
import br.com.fiap.ecopark.domain.model.Cliente;
import br.com.fiap.ecopark.domain.service.ClienteService;

public class ClienteControllerImpl implements ClienteController {

    private final ClienteService clienteService;

    public ClienteControllerImpl(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Override
    public Cliente criarCliente(Cliente clienteInput) {
        return this.clienteService.criar(clienteInput);
    }

    @Override
    public Cliente atualizarCliente(Cliente clienteInput) {
        return null;
    }

    @Override
    public void delete(String cpf, Integer versao) {

    }

    @Override
    public Cliente buscarById(String cpf) throws EntidadeNaoLocalizada {
        return this.clienteService.localizar(cpf);
    }
}
