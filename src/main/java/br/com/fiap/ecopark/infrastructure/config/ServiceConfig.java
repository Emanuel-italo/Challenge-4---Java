
package br.com.fiap.ecopark.infrastructure.config;

import br.com.fiap.ecopark.application.service.ClienteServiceImpl;
import br.com.fiap.ecopark.domain.repository.ClienteRepository;
import br.com.fiap.ecopark.domain.service.ClienteService;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ServiceConfig {

    @ApplicationScoped
    public ClienteService clienteService(ClienteRepository clienteRepository) {
        return new ClienteServiceImpl(clienteRepository);
    }
}
