package br.com.fiap.ecopark.domain.model;

import br.com.fiap.ecopark.domain.exceptions.ValidacaoDominioException;

public class Endereco {

    private String cep;

    private String numero;

    private String complemento;

    public Endereco(String cep, String numero, String complemento) {
        this.cep = cep;
        validaCep();
        this.numero = numero;
        this.complemento = complemento;
    }

    private void validaCep(){
        if(!this.cep.matches("\\d{5}-\\d{3}")){
            throw new ValidacaoDominioException("CEP inválido, utilize o formato XXXXX-XXX");
        };
    }

    public String getCep() {
        return cep;
    }

    public String getNumero() {
        return numero;
    }

    public String getComplemento() {
        return complemento;
    }
}
