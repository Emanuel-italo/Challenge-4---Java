package br.com.fiap.ecopark.infrastructure.persistence;

import br.com.fiap.ecopark.domain.exceptions.EntidadeNaoLocalizada;
import br.com.fiap.ecopark.domain.model.Carro;
import br.com.fiap.ecopark.domain.model.Contrato;
import br.com.fiap.ecopark.domain.repository.ContratoRepository;
import br.com.fiap.ecopark.infrastructure.exceptions.InfraestruturaException;
import jakarta.transaction.Transactional;

import java.sql.*;
import java.util.List;

public class JdbcContratoRepository implements ContratoRepository {

    private final DatabaseConnection databaseConnection;

    public JdbcContratoRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public Contrato salvar(Contrato contrato) {

        Connection conn = null;
        PreparedStatement stmtContrato = null;
        PreparedStatement stmtCarro = null;

        try {
            conn = this.databaseConnection.getConnection();

            conn.setAutoCommit(false);


            // fazendo o insert de contrato

            String sqlContrato = """
                    INSERT INTO CONTRATO ( CPF, DATA_INICIO, DATA_FIM, VERSION,CREATED_AT,LAST_UPDATE)
                    VALUES (?, ?, ?, ?, ?,?)
                    """;

            stmtContrato = conn.prepareStatement(sqlContrato, new String[]{"ID"});
            stmtContrato.setString(1, contrato.getClienteCpf());

            stmtContrato.setTimestamp(2, Timestamp.valueOf(contrato.getDataInicio()));
            stmtContrato.setTimestamp(3, contrato.getDataFim() != null ?
                    Timestamp.valueOf(contrato.getDataFim()) : null);

            stmtContrato.setLong(4, contrato.getVersao());

            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            stmtContrato.setTimestamp(5, currentTimestamp);
            stmtContrato.setTimestamp(6, currentTimestamp);

            int affectedRows = stmtContrato.executeUpdate();
            if (affectedRows == 0) {
                conn.rollback();
                throw new InfraestruturaException("Erro ao salvar contrato, nenhuma linha foi afetada");
            }

            //recuperar o id do contrato recem inserido
            final Long contratoId;

            try (ResultSet generatedKeys = stmtContrato.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    contratoId = generatedKeys.getLong(1);
                } else {
                    conn.rollback();
                    throw new InfraestruturaException("Erro ao salvar contrato, nenhuma linha foi afetada");
                }
            }

            String sqlCarro = """
                    INSERT INTO CARRO (CONTRATO_ID, MARCA, MODELO, ANO, PLACA, PORTE, CONTRATO_VERSION,CREATED_AT,LAST_UPDATED) VALUES (?, ?, ?, ?, ?, ?,?,?,?)
                    """;

            stmtCarro = conn.prepareStatement(sqlCarro);

            for (Carro carro : contrato.getCarros()) {
                stmtCarro.setLong(1, contratoId);
                stmtCarro.setString(2, carro.getMarca());
                stmtCarro.setString(3, carro.getModelo());
                stmtCarro.setInt(4, carro.getAno());
                stmtCarro.setString(5, carro.getPlaca());
                stmtCarro.setString(6, carro.getPorte().name());
                stmtCarro.setLong(7, contrato.getVersao());
                stmtCarro.setTimestamp(8, currentTimestamp);
                stmtCarro.setTimestamp(9, currentTimestamp);
                stmtCarro.addBatch();
            }

            stmtCarro.executeBatch();

            conn.commit();
            return new Contrato(contratoId, contrato.getClienteCpf(), contrato.getCarros(),
                    contrato.getDataInicio(), contrato.getDataFim(), contrato.getVersao());

        } catch (SQLException e) {
            if(conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    //Adicionar futuramente log
                }
            }
            throw new InfraestruturaException("Erro ao salvar contrato", e);
        } finally {
            try{
                if (stmtContrato != null) {
                    stmtContrato.close();
                }
                if(stmtCarro != null) {
                    stmtCarro.close();
                }
                if(conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            }catch (SQLException ex){
                //Adicionar futuramente log
            }
        }
    }

    @Override
    public Contrato buscarPorId(Long id) throws EntidadeNaoLocalizada {
        return null;
    }

    @Override
    public Contrato editar(Contrato contrato) {
        return null;
    }

    @Override
    public List<Contrato> buscarPorClienteCpf(String cpf) {
        return List.of();
    }

    @Override
    public Contrato buscarAtivoPorClienteCpf(String cpf) {
        return null;
    }

    @Override
    public void finalizar(Long id, Long version) {

    }
}
