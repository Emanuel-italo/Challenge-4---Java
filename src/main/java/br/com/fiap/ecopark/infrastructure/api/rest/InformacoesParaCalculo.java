package br.com.fiap.ecopark.infrastructure.api.rest;

public class InformacoesParaCalculo {

    private Float valor1;

    private Float valor2;

    private String operacao;

    public Float getValor1() {
        return valor1;
    }

    public void setValor1(Float valor1) {
        this.valor1 = valor1;
    }

    public Float getValor2() {
        return valor2;
    }

    public void setValor2(Float valor2) {
        this.valor2 = valor2;
    }

    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }
}
