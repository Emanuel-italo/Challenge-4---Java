package br.com.fiap.ecopark.application.exception;

public class ClienteUnsupportedOperation extends  RuntimeException {

    public ClienteUnsupportedOperation(String message) {
        super(message);
    }
}
