package br.com.fiap.ecopark.infrastructure.api.rest;

public class ResultadoCalculadora {

    private Float resultado;

    public ResultadoCalculadora(Float resultado) {
        this.resultado = resultado;
    }

    public Float getResultado() {
        return resultado;
    }

    public void setResultado(Float resultado) {
        this.resultado = resultado;
    }
}
