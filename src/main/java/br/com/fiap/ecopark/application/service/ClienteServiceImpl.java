package br.com.fiap.ecopark.application.service;


import br.com.fiap.ecopark.application.exception.ClienteUnsupportedOperation;
import br.com.fiap.ecopark.domain.exceptions.ClienteConsistenceError;
import br.com.fiap.ecopark.domain.exceptions.EntidadeNaoLocalizada;
import br.com.fiap.ecopark.domain.model.Cliente;
import br.com.fiap.ecopark.domain.repository.ClienteRepository;
import br.com.fiap.ecopark.domain.service.ClienteContratoVerifier;
import br.com.fiap.ecopark.domain.service.ClienteService;

import java.util.List;

/**
 * Implementação do serviço de Cliente que centraliza todas as operações relacionadas.
 * Esta implementação substitui os múltiplos use cases individuais.
 */
public final class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteContratoVerifier contratoVerifier;

    public ClienteServiceImpl(ClienteRepository clienteRepository, ClienteContratoVerifier contratoVerifier) {
        this.clienteRepository = clienteRepository;
        this.contratoVerifier = contratoVerifier;
    }

    @Override
    public Cliente criar(Cliente cliente) {
        try {
            localizar(cliente.getCpf());
            throw new ClienteUnsupportedOperation("Cliente já cadastrado");
        } catch (EntidadeNaoLocalizada e) {
            return clienteRepository.salvar(cliente);
        }
    }

    @Override
    public Cliente editar(Cliente cliente) {
        try {
            Cliente clienteExistente = localizar(cliente.getCpf());

            if (!clienteExistente.getVersao().equals(cliente.getVersao())) {
                throw new EntidadeNaoLocalizada("Versão do cliente desatualizada");
            }

            if (!clienteExistente.isAtivo()) {
                throw new EntidadeNaoLocalizada("Cliente inativo não pode ser editado");
            }

            return clienteRepository.salvar(cliente);
        } catch (EntidadeNaoLocalizada e) {
            throw new ClienteUnsupportedOperation("Cliente não encontrado");
        }
    }

    @Override
    public void desativar(String cpf, Long versao) {
        // Verifica se existe contrato ativo para o cliente
        if (contratoVerifier.hasActiveContract(cpf)) {
            throw new ClienteUnsupportedOperation("Cliente com contrato ativo não pode ser desativado");
        }

        // Não há contrato ativo, pode desativar o cliente
        try {
            Cliente cliente = localizar(cpf);

            if (!cliente.getVersao().equals(versao)) {
                throw new ClienteUnsupportedOperation("Versão do cliente desatualizada");
            }

            if (!cliente.isAtivo()) {
                throw new ClienteUnsupportedOperation("Cliente já está inativo");
            }

            cliente.desativar();
            clienteRepository.salvar(cliente);
        } catch (EntidadeNaoLocalizada ex) {
            throw new ClienteUnsupportedOperation("Cliente não encontrado");
        }
    }

    @Override
    public void reativar(String cpf, Long versao) throws ClienteConsistenceError {
        try {
            Cliente cliente = localizar(cpf);

            if (!cliente.getVersao().equals(versao)) {
                throw new ClienteConsistenceError("Versão do cliente desatualizada");
            }

            if (cliente.isAtivo()) {
                throw new ClienteConsistenceError("Cliente já está ativo");
            }

            cliente.reativar();
            clienteRepository.salvar(cliente);
        } catch (EntidadeNaoLocalizada e) {
            throw new ClienteConsistenceError("Cliente não encontrado");
        }
    }

    @Override
    public Cliente localizar(String cpf) throws EntidadeNaoLocalizada {
        return clienteRepository.buscarPorCpf(cpf);
    }

    @Override
    public List<Cliente> listarTodos() {
        return clienteRepository.buscarTodos();
    }
}
