package br.com.fiap.ecopark.interfaces;

import br.com.fiap.ecopark.domain.service.ContratoService;

public class ContratoControllerImpl implements ContratoController{

    private final ContratoService contratoService;

    public ContratoControllerImpl(ContratoService contratoService) {
        this.contratoService = contratoService;
    }
}
