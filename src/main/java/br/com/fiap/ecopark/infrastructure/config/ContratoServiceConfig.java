package br.com.fiap.ecopark.infrastructure.config;


import br.com.fiap.ecopark.application.service.ContratoServiceImpl;
import br.com.fiap.ecopark.application.service.ContratoServiceImplV2;
import br.com.fiap.ecopark.domain.logging.Logger;
import br.com.fiap.ecopark.domain.repository.ContratoRepository;
import br.com.fiap.ecopark.domain.service.ClienteService;
import br.com.fiap.ecopark.domain.service.ContratoService;
import br.com.fiap.ecopark.infrastructure.logging.LoggerFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;


@ApplicationScoped
public class ContratoServiceConfig {

    private final ContratoRepository contratoRepository;
    private final ClienteService clienteService;

    public ContratoServiceConfig(ContratoRepository contratoRepository, ClienteService clienteService) {
        this.contratoRepository = contratoRepository;
        this.clienteService = clienteService;
    }

    @RequestScoped
    public ContratoService contratoService() {
        final Logger logger = LoggerFactory.getLogger(this.getClass());
        return new ContratoServiceImpl(contratoRepository, clienteService, logger);
    }
}
