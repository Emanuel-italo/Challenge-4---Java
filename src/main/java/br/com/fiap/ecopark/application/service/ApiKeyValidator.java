package br.com.fiap.ecopark.application.service;

public interface ApiKeyValidator {

    boolean isValid(String apiKey);

    boolean isPresent(String apikey);
}
