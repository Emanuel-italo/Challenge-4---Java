package br.com.fiap.ecopark.application.service;

import br.com.fiap.ecopark.domain.service.ClienteContratoVerifier;

public class ClienteContratoVerifierImpl implements ClienteContratoVerifier {
    @Override
    public boolean hasActiveContract(String cpfCliente) {
        return false;
    }
}
