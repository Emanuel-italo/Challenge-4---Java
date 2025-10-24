package br.com.fiap.ecopark.infrastructure.config;

import br.com.fiap.ecopark.application.service.ApiKeyValidator;
import br.com.fiap.ecopark.infrastructure.security.ApiKeyValidatorImpl;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class SecurityConfig {

    @ApplicationScoped
    public ApiKeyValidator apiKeyValidator(@ConfigProperty(name = "api.key") String validApiKey) {
        return new ApiKeyValidatorImpl(validApiKey);
    }

}
