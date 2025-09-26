package br.com.fiap.ecopark.infrastructure.config;

import br.com.fiap.ecopark.domain.repository.ClienteRepository;
import br.com.fiap.ecopark.domain.repository.ContratoRepository;
import br.com.fiap.ecopark.infrastructure.persistence.DatabaseConnection;
import br.com.fiap.ecopark.infrastructure.persistence.DatabaseConnectionImpl;
import br.com.fiap.ecopark.infrastructure.persistence.JdbcClienteRepository;
import br.com.fiap.ecopark.infrastructure.persistence.JdbcContratoRepository;
import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DatabaseConfig {

    @ApplicationScoped
    public DatabaseConnection databaseConnection(AgroalDataSource dataSource) {
        return new DatabaseConnectionImpl(dataSource);
    }

    @ApplicationScoped
    public ContratoRepository contratoRepository(DatabaseConnection databaseConnection) {
        return new JdbcContratoRepository(databaseConnection);
    }

    @ApplicationScoped
    public ClienteRepository clienteRepository(DatabaseConnection databaseConnection) {
        return new JdbcClienteRepository(databaseConnection);
    }

}
