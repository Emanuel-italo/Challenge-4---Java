package br.com.fiap.ecopark.infrastructure.persistence;

import br.com.fiap.ecopark.domain.exceptions.EntidadeNaoLocalizada;
import br.com.fiap.ecopark.domain.model.Cliente;
import br.com.fiap.ecopark.domain.model.Endereco;
import br.com.fiap.ecopark.domain.repository.ClienteRepository;
import br.com.fiap.ecopark.infrastructure.exceptions.InfraestruturaException;

import java.sql.*;
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

        try (Connection conn = this.databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getEmail());
            stmt.setInt(5, cliente.getAnoNascimento());
            stmt.setBoolean(6, cliente.isAtivo());
            stmt.setLong(7, cliente.getVersao());
            stmt.setString(8, cliente.getEndereco().getCep());
            stmt.setString(9, cliente.getEndereco().getNumero());
            stmt.setString(10, cliente.getEndereco().getComplemento());

            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            stmt.setTimestamp(11, currentTimestamp);
            stmt.setTimestamp(12, currentTimestamp);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new InfraestruturaException("Erro ao salvar, nenhuma linha da banco foi afetada");
            }

            return cliente;

        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao salvar cliente", e);
        }
    }

    @Override
    public Cliente buscarPorCpf(String cpf) throws EntidadeNaoLocalizada {

        String sql = """
                SELECT NOME, CPF, TELEFONE, EMAIL, ANO_NASCIMENTO, ATIVO, VERSION,
                CEP, NUMERO, COMPLEMENTO FROM CLIENTE WHERE CPF = ?
                """;

        try (Connection conn = this.databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);

            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                String nome = resultSet.getString("NOME");
                String cpfFromDB = resultSet.getString("CPF");
                Integer anoNascimento = resultSet.getInt("ANO_NASCIMENTO");
                String telefone = resultSet.getString("TELEFONE");
                String email = resultSet.getString("EMAIL");
                Long versao = resultSet.getLong("VERSION");
                String cep = resultSet.getString("CEP");
                String complemento = resultSet.getString("COMPLEMENTO");
                String numero = resultSet.getString("NUMERO");
                Boolean ativo = resultSet.getBoolean("ATIVO");

                Endereco endereco = new Endereco(cep, numero, complemento);

                resultSet.close();

                return new Cliente(nome, cpfFromDB, anoNascimento, telefone, email, endereco, ativo, versao);
            }

        } catch (SQLException e) {
            throw new EntidadeNaoLocalizada("Erro ao buscar cliente por cpf", e);
        }
        throw new EntidadeNaoLocalizada("Cliente nao encontrado");
    }

    @Override
    public Cliente editar(Cliente cliente) {
        return null;
    }

    @Override
    public List<Cliente> buscarTodos() {
        return List.of();
    }

    @Override
    public void desativar(String cpf, Long versao) {

    }

    @Override
    public void reativar(String cpf, Long versao) {

    }
}
