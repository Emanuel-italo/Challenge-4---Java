package br.com.fiap.ecopark.domain.service;

/**
 * Interface para verificação de contratos ativos de clientes.
 * Esta interface é usada para quebrar a dependência circular entre ClienteService e ContratoService.
 */
public interface ClienteContratoVerifier {
    
    /**
     * Verifica se um cliente possui contrato ativo.
     * 
     * @param cpfCliente CPF do cliente a ser verificado
     * @return true se o cliente possui contrato ativo, false caso contrário
     */
    boolean hasActiveContract(String cpfCliente);
}
