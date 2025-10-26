package br.com.fiap.ecopark.infrastructure.config;

import br.com.fiap.ecopark.domain.service.ClienteService;
import br.com.fiap.ecopark.domain.service.ContratoService;
import br.com.fiap.ecopark.interfaces.ClienteController;
import br.com.fiap.ecopark.interfaces.ClienteControllerImpl;
import br.com.fiap.ecopark.interfaces.ContratoController;
import br.com.fiap.ecopark.interfaces.ContratoControllerImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Qualifier;

@ApplicationScoped
public class ControllerConfig {

    @ApplicationScoped
    public ClienteController clienteController(ClienteService clienteService) {
        return new ClienteControllerImpl(clienteService);
    }

}
