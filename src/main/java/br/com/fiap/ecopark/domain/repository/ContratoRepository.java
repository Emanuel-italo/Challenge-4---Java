package br.com.fiap.ecopark.domain.repository;

import br.com.fiap.ecopark.domain.exceptions.EntidadeNaoLocalizada;
import br.com.fiap.ecopark.domain.model.Contrato;

import java.util.List;

public interface ContratoRepository {
    Contrato salvar(Contrato contrato);
    Contrato buscarPorId(Long id) throws EntidadeNaoLocalizada;
    Contrato editar(Contrato contrato);
    List<Contrato> buscarPorClienteCpf(String cpf);
    Contrato buscarAtivoPorClienteCpf(String cpf);
    void finalizar(Long id, Long version);
}
