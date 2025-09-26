package br.com.fiap.ecopark.infrastructure.main;

import br.com.fiap.ecopark.domain.exceptions.EntidadeNaoLocalizada;
import br.com.fiap.ecopark.domain.model.Cliente;
import br.com.fiap.ecopark.domain.repository.ClienteRepository;
import io.quarkus.runtime.QuarkusApplication;
import jakarta.inject.Inject;

public class EcoParkApplication implements QuarkusApplication {

    @Inject
    ClienteRepository clienteRepository;

    @Override
    public int run(String... args) throws Exception {
        findByCpf("92417168006");
        createCliente();
        updateCliente();
        deleteCliente();
        return 0;
    }

    //TODO
    private void deleteCliente() {
    }

    //TODO
    private void updateCliente() {

    }

    //TODO
    private void createCliente() {

    }

    private void findByCpf(String cpf) {
        try {
            Cliente cliente = this.clienteRepository.buscarPorCpf(cpf);
            System.out.println("Cliente localizado:");
            System.out.println(cliente.toString());
        } catch (EntidadeNaoLocalizada e) {
            System.out.println("Cliente nao localizado");
        }
    }
}
