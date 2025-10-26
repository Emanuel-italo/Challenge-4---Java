package br.com.fiap.ecopark.application.service;

import br.com.fiap.ecopark.domain.exceptions.EntidadeNaoLocalizada;
import br.com.fiap.ecopark.domain.model.Contrato;
import br.com.fiap.ecopark.domain.service.ContratoService;

import java.util.List;

public class ContratoServiceImplV2 implements ContratoService {
    @Override
    public Contrato criar(Contrato contrato) {
        return null;
    }

    @Override
    public Contrato editar(Contrato contrato) {
        return null;
    }

    @Override
    public void finalizar(Long id, Long versao) throws EntidadeNaoLocalizada {

    }

    @Override
    public List<Contrato> listarContratosCliente(String cpfCliente) {
        return List.of();
    }

    @Override
    public Contrato buscarContratoAtivoCliente(String cpfCliente) throws EntidadeNaoLocalizada {
        return null;
    }
}
