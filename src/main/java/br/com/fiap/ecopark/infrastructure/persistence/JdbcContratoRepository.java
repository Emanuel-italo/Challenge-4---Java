package br.com.fiap.ecopark.infrastructure.persistence;

import br.com.fiap.ecopark.domain.exceptions.EntidadeNaoLocalizada;
import br.com.fiap.ecopark.domain.model.Carro;
import br.com.fiap.ecopark.domain.model.CarroPorte;
import br.com.fiap.ecopark.domain.model.Contrato;
import br.com.fiap.ecopark.domain.repository.ContratoRepository;
import br.com.fiap.ecopark.infrastructure.exceptions.InfraestruturaException;
import jakarta.transaction.Transactional;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            conn = databaseConnection.getConnection();
            conn.setAutoCommit(false); // Inicia transação

            // Insere o contrato
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
                throw new InfraestruturaException("Falha ao criar contrato, nenhuma linha afetada.");
            }

            // Obtém o ID gerado para o contrato
            final Long contratoId;
            try (ResultSet generatedKeys = stmtContrato.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    contratoId = generatedKeys.getLong(1);
                } else {
                    throw new InfraestruturaException("Falha ao obter ID do contrato.");
                }
            }


            // Insere os carros do contrato
            String sqlCarro = """
                    INSERT INTO CARRO (CONTRATO_ID, MARCA, MODELO, ANO, PLACA, PORTE, CONTRATO_VERSION,CREATED_AT,LAST_UPDATED)
                    VALUES (?, ?, ?, ?, ?, ?,?,?,?)
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

            conn.commit(); // Confirma a transação

            // Retorna o contrato com o ID atualizado
            return new Contrato(contratoId, contrato.getClienteCpf(), contrato.getCarros(),
                    contrato.getDataInicio(), contrato.getDataFim(), contrato.getVersao());

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Desfaz a transação em caso de erro
                } catch (SQLException ex) {
                    //vamos por um log aqui
                }
            }
            throw new InfraestruturaException("Erro ao salvar contrato: " + e.getMessage(), e);
        } finally {
            // Fecha os recursos
            try {
                if (stmtCarro != null) stmtCarro.close();
                if (stmtContrato != null) stmtContrato.close();
                if (conn != null) {
                    conn.setAutoCommit(true); // Restaura o modo auto-commit
                    conn.close();
                }
            } catch (SQLException e) {
                //vamos por um log aqui
            }
        }
    }

    @Override
    public Contrato buscarPorId(Long id) throws EntidadeNaoLocalizada {

        String sqlContrato = """
                SELECT ID, CPF, DATA_INICIO, DATA_FIM, VERSION
                FROM CONTRATO WHERE ID = ?
                """;

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlContrato)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Recupera os dados básicos do contrato
                    Long contratoId = rs.getLong("id");
                    String clienteCpf = rs.getString("cliente_cpf");
                    LocalDateTime dataInicio = rs.getTimestamp("data_inicio").toLocalDateTime();
                    Timestamp dataFimTs = rs.getTimestamp("data_fim");
                    LocalDateTime dataFim = dataFimTs != null ? dataFimTs.toLocalDateTime() : null;
                    Long versao = rs.getLong("version");

                    // Recupera os carros associados ao contrato
                    Set<Carro> carros = buscarCarrosPorContratoId(conn, contratoId, versao);

                    Contrato contrato = new Contrato(contratoId, clienteCpf, carros, dataInicio, dataFim, versao);
                    return contrato;
                } else {
                    throw new EntidadeNaoLocalizada("Contrato não encontrado para o ID: " + id);
                }
            }
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao buscar contrato por ID: " + e.getMessage(), e);
        }
    }

    @Override
    public Contrato editar(Contrato contrato) {

        Connection conn = null;
        PreparedStatement stmtContrato = null;
        PreparedStatement stmtUpdateCarrosAntigos = null;
        PreparedStatement stmtInsertCarros = null;

        try {
            conn = databaseConnection.getConnection();
            conn.setAutoCommit(false); // Inicia transação

            // Atualiza o contrato
            String sqlContrato = """
                    UPDATE CONTRATO SET VERSION = ?, LAST_UPDATE = ?
                    WHERE ID = ? AND VERSION = ?
                    """;

            stmtContrato = conn.prepareStatement(sqlContrato);
            stmtContrato.setLong(1, contrato.getVersao());

            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            stmtContrato.setTimestamp(2, currentTimestamp);
            stmtContrato.setLong(3, contrato.getId());
            stmtContrato.setLong(4, contrato.getVersao() - 1);

            int affectedRows = stmtContrato.executeUpdate();
            if (affectedRows == 0) {
                throw new InfraestruturaException("Falha ao editar contrato. O contrato pode ter sido modificado por outro processo.");
            }

            // Update carros antigos
            String sqlUpdateCarrosAntigos = """
                    UPDATE CARRO SET LAST_UPDATED = ? WHERE contrato_id = ? AND contrato_version = ?
                    """;
            stmtUpdateCarrosAntigos = conn.prepareStatement(sqlUpdateCarrosAntigos);
            stmtUpdateCarrosAntigos.setTimestamp(1, currentTimestamp);
            stmtUpdateCarrosAntigos.setLong(2, contrato.getId());
            stmtUpdateCarrosAntigos.setLong(3, contrato.getVersao() - 1);
            stmtUpdateCarrosAntigos.executeUpdate();

            // Insere os carros atualizados
            String sqlInsertCarros = """
                    INSERT INTO CARRO (CONTRATO_ID, MARCA, MODELO, ANO, PLACA, PORTE, CONTRATO_VERSION,CREATED_AT, LAST_UPDATED)
                    VALUES (?, ?, ?, ?, ?, ?,?,?,?)
                    """;

            stmtInsertCarros = conn.prepareStatement(sqlInsertCarros);

            for (Carro carro : contrato.getCarros()) {
                stmtInsertCarros.setLong(1, contrato.getId());
                stmtInsertCarros.setString(2, carro.getMarca());
                stmtInsertCarros.setString(3, carro.getModelo());
                stmtInsertCarros.setInt(4, carro.getAno());
                stmtInsertCarros.setString(5, carro.getPlaca());
                stmtInsertCarros.setString(6, carro.getPorte().name());
                stmtInsertCarros.setLong(7, contrato.getVersao());
                stmtInsertCarros.setTimestamp(8, currentTimestamp);
                stmtInsertCarros.setTimestamp(9, currentTimestamp);
                stmtInsertCarros.addBatch();
            }

            stmtInsertCarros.executeBatch();

            conn.commit(); // Confirma a transação

            // Incrementa a versão do contrato
            contrato.incrementarVersao();

            return contrato;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Desfaz a transação em caso de erro
                } catch (SQLException ex) {
                    //vamos por um log aqui
                }
            }
            throw new InfraestruturaException("Erro ao editar contrato: " + e.getMessage(), e);
        } finally {
            // Fecha os recursos
            try {
                if (stmtInsertCarros != null) stmtInsertCarros.close();
                if (stmtUpdateCarrosAntigos != null) stmtUpdateCarrosAntigos.close();
                if (stmtContrato != null) stmtContrato.close();
                if (conn != null) {
                    conn.setAutoCommit(true); // Restaura o modo auto-commit
                    conn.close();
                }
            } catch (SQLException e) {
                //vamos por um log aqui
            }
        }
    }

    // Método auxiliar para buscar os carros associados a um contrato
    private Set<Carro> buscarCarrosPorContratoId(Connection conn, Long contratoId, Long contratoVersion) throws SQLException {
        String sql = """
                SELECT MARCA, MODELO, ANO, PLACA, PORTE
                FROM CARRO WHERE CONTRATO_ID = ? AND CONTRATO_VERSION = ?
                """;

        Set<Carro> carros = new HashSet<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, contratoId);
            stmt.setLong(2, contratoVersion);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String marca = rs.getString("marca");
                    String modelo = rs.getString("modelo");
                    int ano = rs.getInt("ano");
                    String placa = rs.getString("placa");
                    String porteStr = rs.getString("porte");
                    CarroPorte porte = CarroPorte.valueOf(porteStr);

                    Carro carro = new Carro(marca, modelo, ano, placa, porte);
                    carros.add(carro);
                }
            }
        }

        return carros;
    }

    @Override
    public List<Contrato> buscarPorClienteCpf(String cpf) {

        String sql = """
                SELECT ID, CPF, DATA_INICIO, DATA_FIM, VERSION
                FROM CONTRATO WHERE CPF = ?
                """;

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                List<Contrato> contratos = new ArrayList<>();

                while (rs.next()) {
                    Long id = rs.getLong("id");
                    String clienteCpf = rs.getString("cpf");
                    LocalDateTime dataInicio = rs.getTimestamp("data_inicio").toLocalDateTime();
                    Timestamp dataFimTs = rs.getTimestamp("data_fim");
                    LocalDateTime dataFim = dataFimTs != null ? dataFimTs.toLocalDateTime() : null;
                    Long versao = rs.getLong("version");

                    Set<Carro> carros = buscarCarrosPorContratoId(conn, id, versao);

                    Contrato contrato = new Contrato(id, clienteCpf, carros, dataInicio, dataFim, versao);
                    contratos.add(contrato);
                }

                return contratos;
            }
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao buscar contratos por CPF: " + e.getMessage(), e);
        }
    }

    @Override
    public Contrato buscarAtivoPorClienteCpf(String cpf) {

        String sql = """
                SELECT ID, CPF, DATA_INICIO, DATA_FIM, VERSION
                FROM CONTRATO WHERE CPF = ? AND DATA_FIM IS NULL
                """;

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Long id = rs.getLong("id");
                    String clienteCpf = rs.getString("cpf");
                    LocalDateTime dataInicio = rs.getTimestamp("data_inicio").toLocalDateTime();
                    Long versao = rs.getLong("version");

                    Set<Carro> carros = buscarCarrosPorContratoId(conn, id, versao);

                    Contrato contrato = new Contrato(id, clienteCpf, carros, dataInicio, null, versao);
                    return contrato;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao buscar contrato ativo por CPF: " + e.getMessage(), e);
        }
    }

    @Override
    public void finalizar(Long id, Long version) {

        String sql = """
                UPDATE CONTRATO SET DATA_FIM = ?, VERSION = ?, LAST_UPDATE = ?
                WHERE ID = ? AND VERSION = ?
                """;

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setLong(2, version);
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            stmt.setTimestamp(3, currentTimestamp);
            stmt.setLong(4, id);
            stmt.setLong(5, version - 1);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new InfraestruturaException("Falha ao finalizar contrato. O contrato pode ter sido modificado por outro processo.");
            }

        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao finalizar contrato: " + e.getMessage(), e);
        }
    }
}
