package br.com.fiap.ecopark.infrastructure.config;


import br.com.fiap.ecopark.application.service.ClienteServiceImpl;
import br.com.fiap.ecopark.domain.repository.ClienteRepository;
import br.com.fiap.ecopark.domain.service.ClienteContratoVerifier;
import br.com.fiap.ecopark.domain.service.ClienteService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;

@ApplicationScoped
public class ClienteServiceConfig {

    private final ClienteRepository clienteRepository;
    private final ClienteContratoVerifier contratoVerifier;

    public ClienteServiceConfig(ClienteRepository clienteRepository, ClienteContratoVerifier contratoVerifier) {
        this.clienteRepository = clienteRepository;
        this.contratoVerifier = contratoVerifier;
    }

    @RequestScoped
    public ClienteService clienteService() {
        return new ClienteServiceImpl(clienteRepository, contratoVerifier);
    }
}
