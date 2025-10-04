package br.com.fiap.ecopark.infrastructure.persistence;

import br.com.fiap.ecopark.domain.exceptions.EntidadeNaoLocalizada;
import br.com.fiap.ecopark.domain.model.Cliente;
import br.com.fiap.ecopark.domain.model.Endereco;
import br.com.fiap.ecopark.domain.repository.ClienteRepository;
import br.com.fiap.ecopark.infrastructure.exceptions.InfraestruturaException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcClienteRepository implements ClienteRepository {

    private final DatabaseConnection databaseConnection;

    public JdbcClienteRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }
    @Override
    public Cliente salvar(Cliente cliente) {

        String sql = """
                INSERT INTO CLIENTE (NOME, CPF, TELEFONE, EMAIL, ANO_NASCIMENTO, ATIVO, VERSION,
                CEP, NUMERO, COMPLEMENTO,CREATED_AT, LAST_UPDATE)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)
                """;

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getEmail());
            stmt.setInt(5, cliente.getAnoNascimento());
            stmt.setBoolean(6, cliente.isAtivo());
            stmt.setLong(7, cliente.getVersao());

            // Dados do endereço
            Endereco endereco = cliente.getEndereco();
            stmt.setString(8, endereco.getCep());
            stmt.setString(9, endereco.getNumero());
            stmt.setString(10, endereco.getComplemento());
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            stmt.setTimestamp(11, currentTimestamp);
            stmt.setTimestamp(12, currentTimestamp);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new InfraestruturaException("Falha ao criar cliente, nenhuma linha afetada.");
            }

            return cliente;
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao salvar cliente: " + e.getMessage(), e);
        }
    }

    @Override
    public Cliente buscarPorCpf(String cpf) throws EntidadeNaoLocalizada {

        String sql = """
                SELECT NOME, CPF, TELEFONE, EMAIL, ANO_NASCIMENTO, ATIVO, VERSION,
                CEP, NUMERO, COMPLEMENTO FROM CLIENTE WHERE CPF = ?
                """;

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = mapearCliente(rs);
                    return cliente;
                } else {
                    throw new EntidadeNaoLocalizada("Cliente não encontrado para o CPF: " + cpf);
                }
            }
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao buscar cliente por CPF: " + e.getMessage(), e);
        }
    }

    @Override
    public Cliente editar(Cliente cliente) {

        String sql = """
                UPDATE CLIENTE SET NOME = ?, TELEFONE = ?, EMAIL = ?, ANO_NASCIMENTO = ?,
                CEP = ?, NUMERO = ?, COMPLEMENTO = ?, VERSION = ?, LAST_UPDATE = ?
                WHERE CPF = ? AND VERSION = ?
                """;

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getTelefone());
            stmt.setString(3, cliente.getEmail());
            stmt.setInt(4, cliente.getAnoNascimento());

            // Dados do endereço
            Endereco endereco = cliente.getEndereco();
            stmt.setString(5, endereco.getCep());
            stmt.setString(6, endereco.getNumero());
            stmt.setString(7, endereco.getComplemento());

            stmt.setLong(8, cliente.getVersao());
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            stmt.setTimestamp(9, currentTimestamp);

            stmt.setString(10, cliente.getCpf());
            stmt.setLong(11, cliente.getVersao() - 1);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new InfraestruturaException(
                        "Falha ao editar cliente. O cliente pode ter sido modificado por outro processo.");
            }

            cliente.incrementarVersao();
            return cliente;
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao editar cliente: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Cliente> buscarTodos() {

        String sql = """
                SELECT NOME, CPF, ATIVO
                FROM CLIENTE ORDER BY NOME
                """;

        try (Connection conn = databaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            List<Cliente> clientes = new ArrayList<>();

            while (rs.next()) {
                Cliente cliente = mapearClienteSimples(rs);
                clientes.add(cliente);
            }

            return clientes;
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao buscar todos os clientes: " + e.getMessage(), e);
        }
    }

    @Override
    public void desativar(String cpf, Long versao) {

        String sql = """
                UPDATE CLIENTE SET ATIVO = FALSE, VERSION = ?, LAST_UPDATE = ?
                WHERE CPF = ? AND VERSION = ?
                """;

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, versao);

            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            stmt.setTimestamp(2, currentTimestamp);

            stmt.setString(3, cpf);
            stmt.setLong(4, versao - 1);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new InfraestruturaException(
                        "Falha ao desativar cliente. O cliente pode ter sido modificado por outro processo.");
            }

        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao desativar cliente: " + e.getMessage(), e);
        }
    }

    @Override
    public void reativar(String cpf, Long versao) throws EntidadeNaoLocalizada {

        String sql = """
                UPDATE CLIENTE SET ATIVO = TRUE, VERSION = ?, LAST_UPDATE = ?
                WHERE CPF = ? AND VERSION = ?
                """;

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            stmt.setTimestamp(2, currentTimestamp);
            stmt.setLong(3, versao);
            stmt.setLong(4, versao - 1);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new EntidadeNaoLocalizada("Cliente não encontrado para o CPF: " + cpf);
            }

        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao reativar cliente: " + e.getMessage(), e);
        }
    }

    // Método auxiliar para mapear um ResultSet para um objeto Cliente
    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        String nome = rs.getString("nome");
        String cpf = rs.getString("cpf");
        String telefone = rs.getString("telefone");
        String email = rs.getString("email");
        Integer anoNascimento = rs.getInt("ano_nascimento");
        Boolean ativo = rs.getBoolean("ativo");
        Long versao = rs.getLong("version");

        // Dados do endereço
        String cep = rs.getString("cep");
        String numero = rs.getString("numero");
        String complemento = rs.getString("complemento");
        Endereco endereco = new Endereco(cep, numero, complemento);

        return new Cliente(nome, cpf, anoNascimento, telefone, email, endereco, ativo, versao);
    }

    private Cliente mapearClienteSimples(ResultSet rs) throws SQLException {
        String nome = rs.getString("nome");
        String cpf = rs.getString("cpf");
        Boolean ativo = rs.getBoolean("ativo");

        return new Cliente(nome, cpf, ativo);
    }
}
