package br.com.fiap.ecopark.infrastructure.config;

import br.com.fiap.ecopark.application.service.ClienteContratoVerifierImpl;
import br.com.fiap.ecopark.domain.service.ClienteContratoVerifier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;

@ApplicationScoped
public class ClienteContratoServiceConfig {

    @RequestScoped
    public ClienteContratoVerifier clienteContratoVerifier(){
        return new ClienteContratoVerifierImpl();
    }
}
