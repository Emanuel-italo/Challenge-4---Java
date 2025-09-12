package br.com.fiap.ecopark.domain.exceptions;

public class EntidadeNaoLocalizada extends Exception{

    public EntidadeNaoLocalizada(String message) {
        super(message);
    }

    public EntidadeNaoLocalizada(String message, Throwable cause) {
        super(message, cause);
    }
}
