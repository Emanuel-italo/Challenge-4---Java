package br.com.fiap.ecopark.domain.model;

import br.com.fiap.ecopark.domain.exceptions.ValidacaoDominioException;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

public class Contrato {

    private Long id;

    private String clienteCpf;

    private Set<Carro> carros;

    private LocalDateTime dataInicio;

    private LocalDateTime dataFim;

    private Long versao;

    public Contrato(Long id, String clienteCpf, Set<Carro> carros, LocalDateTime dataInicio, Long versao) {
        this.id = id;
        setClienteCpf(clienteCpf);
        setCarros(carros);
        this.dataInicio = dataInicio;
        isDataInicioValidParaNovoContrato();
        setVersao(versao);
    }

    public Contrato(Long id, String clienteCpf, Set<Carro> carros, LocalDateTime dataInicio, LocalDateTime dataFim, Long versao) {
        this.id = id;
        setClienteCpf(clienteCpf);
        setCarros(carros);
        this.dataInicio = dataInicio;
        isDataInicioValid();
        this.dataFim = dataFim;
        setVersao(versao);
    }

    public Contrato(Long id, LocalDateTime dataInicio, LocalDateTime dataFim) {
        this.id = id;
        this.dataInicio = dataInicio;
        isDataInicioValid();
        this.dataFim = dataFim;
    }

    public void setCarros(Set<Carro> carros) {
        this.carros = carros;
        validaQuantidadeCarros();
    }

    private void validaQuantidadeCarros() {
        if (carros.size() > 2) {
            throw new ValidacaoDominioException("Cliente pode ter no máximo 2 carros cadastrados");
        }
    }

    private void finalizarContrato() {
        this.dataFim = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getClienteCpf() {
        return clienteCpf;
    }

    public void setClienteCpf(String clienteCpf) {
        if (clienteCpf == null || clienteCpf.isEmpty()) {
            throw new ValidacaoDominioException("CPF do cliente não pode ser nulo ou vazio");
        }
        Cliente.isCpfValido(clienteCpf);
        this.clienteCpf = clienteCpf;
    }

    public Set<Carro> getCarros() {
        return carros;
    }

    //Cenário que queremos garantir para evitar fraude no seguro do estacionamento
    private void isDataInicioValidParaNovoContrato(){
        isDataInicioValid();
        if (dataInicio.isBefore(LocalDateTime.now())) {
            throw new ValidacaoDominioException("Data de inicio do contrato não pode ser anterior a data atual");
        }
    }

    private void isDataInicioValid(){
        if (dataInicio == null) {
            throw new ValidacaoDominioException("Data de inicio do contrato não pode ser nula");
        }
    }


    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    private void isDataFimValid(){
        if (dataFim != null && dataFim.isBefore(this.dataInicio)) {
            throw new ValidacaoDominioException("Data de fim do contrato não pode ser anterior a data de inicio");
        }
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public boolean isAtivo() {
        return this.dataFim == null;
    }

    public void setVersao(Long versao) {
        this.versao = Objects.requireNonNullElse(versao, 0L);
    }

    public Long getVersao() {
        return versao;
    }

    public void incrementarVersao() {
        if (versao == null) {
            versao = 0L;
        } else {
            versao++;
        }
    }

}
