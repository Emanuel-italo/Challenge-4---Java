package br.com.fiap.ecopark.application.service;

import br.com.fiap.ecopark.domain.exceptions.EntidadeNaoLocalizada;
import br.com.fiap.ecopark.domain.model.Cliente;
import br.com.fiap.ecopark.domain.repository.ClienteRepository;
import br.com.fiap.ecopark.domain.service.ClienteService;

public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }


    @Override
    public Cliente criar(Cliente cliente) {
        try {
            this.clienteRepository.buscarPorCpf(cliente.getCpf());
        } catch (EntidadeNaoLocalizada e) {
            Cliente clienteSalvo = this.clienteRepository.salvar(cliente);
            return clienteSalvo;
        }
        throw new RuntimeException("Cliente já cadastro");
    }

    @Override
    public void desativar(Long id, Long version) {

    }
}
