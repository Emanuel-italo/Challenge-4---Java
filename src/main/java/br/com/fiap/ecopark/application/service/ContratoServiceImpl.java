package br.com.fiap.ecopark.application.service;


import br.com.fiap.ecopark.application.exception.ClienteUnsupportedOperation;
import br.com.fiap.ecopark.domain.exceptions.ClienteConsistenceError;
import br.com.fiap.ecopark.domain.exceptions.EntidadeNaoLocalizada;
import br.com.fiap.ecopark.domain.logging.Logger;
import br.com.fiap.ecopark.domain.model.Contrato;
import br.com.fiap.ecopark.domain.repository.ContratoRepository;
import br.com.fiap.ecopark.domain.service.ClienteContratoVerifier;
import br.com.fiap.ecopark.domain.service.ClienteService;
import br.com.fiap.ecopark.domain.service.ContratoService;

import java.util.List;

/**
 * Implementação do serviço de Contrato que centraliza todas as operações relacionadas.
 * Esta implementação substitui os múltiplos use cases individuais.
 */
public final class ContratoServiceImpl implements ContratoService, ClienteContratoVerifier {

    private final ContratoRepository contratoRepository;
    private final ClienteService clienteService;
    private final Logger logger;

    public ContratoServiceImpl(ContratoRepository contratoRepository, ClienteService clienteService, Logger logger) {
        this.contratoRepository = contratoRepository;
        this.clienteService = clienteService;
        this.logger = logger;
    }

    @Override
    public Contrato criar(Contrato contrato) {
        try {
            clienteService.reativar(contrato.getClienteCpf(), contrato.getVersao());
        } catch (ClienteConsistenceError e) {
            logger.info("Não foi necessário reativar o cliente pois ele já está ativo");
        }
        return contratoRepository.salvar(contrato);
    }

    @Override
    public Contrato editar(Contrato contrato) {
        try {
            Contrato contratoAtivo = buscarContratoAtivoCliente(contrato.getClienteCpf());

            if (!contratoAtivo.getId().equals(contrato.getId())) {
                throw new ClienteUnsupportedOperation("Contrato não corresponde ao contrato ativo do cliente");
            }

            if (!contratoAtivo.getVersao().equals(contrato.getVersao())) {
                throw new ClienteUnsupportedOperation("Versão do contrato desatualizada");
            }

            return contratoRepository.salvar(contrato);
        } catch (EntidadeNaoLocalizada e) {
            throw new ClienteUnsupportedOperation("Contrato ativo não encontrado para o cliente");
        }
    }

    @Override
    public void finalizar(Long id, Long versao) throws EntidadeNaoLocalizada {
        Contrato contrato = contratoRepository.buscarPorId(id);

        if (!contrato.getVersao().equals(versao)) {
            throw new ClienteUnsupportedOperation("Versão do contrato desatualizada");
        }

        if (!contrato.isAtivo()) {
            throw new ClienteUnsupportedOperation("Contrato já está finalizado");
        }

        contrato.finalizar();
        contratoRepository.salvar(contrato);
    }

    @Override
    public List<Contrato> listarContratosCliente(String cpfCliente) {
        return contratoRepository.buscarPorClienteCpf(cpfCliente);
    }

    @Override
    public Contrato buscarContratoAtivoCliente(String cpfCliente) throws EntidadeNaoLocalizada {
        return contratoRepository.buscarAtivoPorClienteCpf(cpfCliente);
    }

    @Override
    public boolean hasActiveContract(String cpfCliente) {
        try {
            buscarContratoAtivoCliente(cpfCliente);
            return true;
        } catch (EntidadeNaoLocalizada e) {
            return false;
        }
    }
}
