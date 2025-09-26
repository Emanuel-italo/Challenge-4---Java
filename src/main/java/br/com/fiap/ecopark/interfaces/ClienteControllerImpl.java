package br.com.fiap.ecopark.interfaces;

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
}
