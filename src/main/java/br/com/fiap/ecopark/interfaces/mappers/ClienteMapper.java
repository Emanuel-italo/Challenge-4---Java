package br.com.fiap.ecopark.interfaces.mappers;

import br.com.fiap.ecopark.domain.model.Cliente;
import br.com.fiap.ecopark.interfaces.dto.output.ClienteOutputDto;

import java.time.LocalDate;

public class ClienteMapper {

    private ClienteMapper(){}

    public static ClienteOutputDto toDto(Cliente cliente){
        ClienteOutputDto clienteDto = new ClienteOutputDto();
        clienteDto.setNome(cliente.getNome());
        clienteDto.setIdade(LocalDate.now().getYear() - cliente.getAnoNascimento());
        clienteDto.setCpf(cliente.getCpf());

        String endereco = cliente.getEndereco().getComplemento() + ", " + cliente.getEndereco().getNumero();
        clienteDto.setEndereco(endereco);
        return clienteDto;
    }
}
