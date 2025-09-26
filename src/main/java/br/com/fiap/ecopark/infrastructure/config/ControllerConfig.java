package br.com.fiap.ecopark.infrastructure.config;

import br.com.fiap.ecopark.domain.service.ClienteService;
import br.com.fiap.ecopark.interfaces.ClienteController;
import br.com.fiap.ecopark.interfaces.ClienteControllerImpl;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ControllerConfig {

    @ApplicationScoped
    public ClienteController clienteController(ClienteService clienteService) {
        return new ClienteControllerImpl(clienteService);
    }
}
