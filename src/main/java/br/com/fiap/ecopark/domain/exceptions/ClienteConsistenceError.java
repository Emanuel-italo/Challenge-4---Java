package br.com.fiap.ecopark.domain.exceptions;

public class ClienteConsistenceError extends Exception {

    public ClienteConsistenceError() {
        super();
    }

    public ClienteConsistenceError(String message) {
        super(message);
    }
}
