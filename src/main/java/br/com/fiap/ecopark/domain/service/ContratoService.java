package br.com.fiap.ecopark.domain.service;



import br.com.fiap.ecopark.domain.exceptions.EntidadeNaoLocalizada;
import br.com.fiap.ecopark.domain.model.Contrato;

import java.util.List;

/**
 * Serviço que centraliza todas as operações relacionadas a Contrato.
 * Esta interface substitui os múltiplos use cases individuais por uma única interface de serviço.
 */
public interface ContratoService {
    
    Contrato criar(Contrato contrato);
    
    Contrato editar(Contrato contrato);
    
    void finalizar(Long id, Long versao) throws EntidadeNaoLocalizada;

    List<Contrato> listarContratosCliente(String cpfCliente);
    
    Contrato buscarContratoAtivoCliente(String cpfCliente) throws EntidadeNaoLocalizada;
}
