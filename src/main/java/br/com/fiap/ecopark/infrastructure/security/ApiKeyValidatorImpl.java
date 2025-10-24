package br.com.fiap.ecopark.infrastructure.security;

import br.com.fiap.ecopark.application.service.ApiKeyValidator;

public class ApiKeyValidatorImpl implements ApiKeyValidator {

    private final String validApiKey;

    public ApiKeyValidatorImpl(String validApiKey) {
        this.validApiKey = validApiKey;
    }

    @Override
    public boolean isValid(String apiKey) {
        if(!isPresent(apiKey)) {
            return false;
        }
        return this.validApiKey.equals(apiKey);
    }

    @Override
    public boolean isPresent(String apiKey) {
        return apiKey != null && !apiKey.trim().isEmpty();
    }
}
