package br.com.fiap.saudetodos.infrastructure.persistence;

import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import br.com.fiap.saudetodos.domain.exceptions.EntidadeNaoLocalizada;
import br.com.fiap.saudetodos.domain.model.Paciente;
import br.com.fiap.saudetodos.domain.repository.PacienteRepository;
import br.com.fiap.saudetodos.infrastructure.exceptions.InfraestruturaException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class JdbcPacienteRepository implements PacienteRepository {

    private final DatabaseConnection conexaoBD;

    @Inject
    public JdbcPacienteRepository(AgroalDataSource ds) {
        this.conexaoBD = new DatabaseConnectionImpl(ds);
    }

    @Override
    public Paciente salvar(Paciente paciente) {
        String sql = "INSERT INTO PACIENTE "
                + "(nome, contato, idade, tipo_deficiencia, telefone, ativo, version, created_at, last_update) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String[] generatedColumns = { "ID" };

        try (Connection conn = conexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, generatedColumns)) {

            stmt.setString(1, paciente.getNome());
            stmt.setString(2, paciente.getContato());
            stmt.setInt(3, paciente.getIdade());
            stmt.setString(4, paciente.getTipoDeficiencia());
            stmt.setString(5, paciente.getTelefone());
            // Oracle NUMBER(1) espera 1 ou 0
            stmt.setInt(6, 1);
            stmt.setLong(7, paciente.getVersao());

            Timestamp agora = new Timestamp(System.currentTimeMillis());
            stmt.setTimestamp(8, agora);
            stmt.setTimestamp(9, agora);

            if (stmt.executeUpdate() == 0) {
                throw new InfraestruturaException("Falha ao salvar paciente.");
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    paciente.setId(rs.getInt(1));
                }
            }

            return paciente;

        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao salvar paciente.", e);
        }
    }

    @Override
    public Paciente buscarPorId(int id) throws EntidadeNaoLocalizada {
        String sql = "SELECT id, nome, contato, idade, tipo_deficiencia, telefone "
                + "FROM PACIENTE WHERE id = ? AND ativo = 1";
        try (Connection conn = conexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new EntidadeNaoLocalizada("Paciente não encontrado: " + id);
                }
                return new Paciente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getInt("idade"),
                        rs.getString("tipo_deficiencia"),
                        rs.getString("telefone")
                );
            }

        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao buscar paciente.", e);
        }
    }

    @Override
    public void editar(Paciente paciente) throws InfraestruturaException, EntidadeNaoLocalizada {
        String sql = "UPDATE PACIENTE SET nome = ?, contato = ?, idade = ?, tipo_deficiencia = ?, "
                + "telefone = ?, version = ?, last_update = ? "
                + "WHERE id = ? AND ativo = 1";
        try (Connection conn = conexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            long novaVersao = paciente.getVersao() + 1;
            Timestamp agora = new Timestamp(System.currentTimeMillis());

            stmt.setString(1, paciente.getNome());
            stmt.setString(2, paciente.getContato());
            stmt.setInt(3, paciente.getIdade());
            stmt.setString(4, paciente.getTipoDeficiencia());
            stmt.setString(5, paciente.getTelefone());
            stmt.setLong(6, novaVersao);
            stmt.setTimestamp(7, agora);
            stmt.setInt(8, paciente.getId());

            if (stmt.executeUpdate() == 0) {
                throw new EntidadeNaoLocalizada("Paciente não encontrado para editar: " + paciente.getId());
            }
            paciente.setVersao(novaVersao);

        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao editar paciente.", e);
        }
    }

    @Override
    public List<Paciente> buscarTodos() {
        String sql = "SELECT id, nome, contato, idade, tipo_deficiencia, telefone "
                + "FROM PACIENTE WHERE ativo = 1 ORDER BY nome";
        List<Paciente> lista = new ArrayList<>();
        try (Connection conn = conexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Paciente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getInt("idade"),
                        rs.getString("tipo_deficiencia"),
                        rs.getString("telefone")
                ));
            }
            return lista;

        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao listar pacientes.", e);
        }
    }

    @Override
    public void desativar(int id, long versao) throws InfraestruturaException, EntidadeNaoLocalizada {
        String sql = "UPDATE PACIENTE SET ativo = 0, version = ?, last_update = ? "
                + "WHERE id = ? AND version = ?";
        try (Connection conn = conexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            long novaVersao = versao + 1;
            Timestamp agora = new Timestamp(System.currentTimeMillis());

            stmt.setLong(1, novaVersao);
            stmt.setTimestamp(2, agora);
            stmt.setInt(3, id);
            stmt.setLong(4, versao);

            if (stmt.executeUpdate() == 0) {
                throw new EntidadeNaoLocalizada("Paciente não encontrado ou versão incorreta: " + id);
            }

        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao desativar paciente.", e);
        }
    }

    @Override
    public void reativar(int id, long versao) throws InfraestruturaException, EntidadeNaoLocalizada {
        String sql = "UPDATE PACIENTE SET ativo = 1, version = ?, last_update = ? "
                + "WHERE id = ? AND version = ?";
        try (Connection conn = conexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            long novaVersao = versao + 1;
            Timestamp agora = new Timestamp(System.currentTimeMillis());

            stmt.setLong(1, novaVersao);
            stmt.setTimestamp(2, agora);
            stmt.setInt(3, id);
            stmt.setLong(4, versao);

            if (stmt.executeUpdate() == 0) {
                throw new EntidadeNaoLocalizada("Paciente não encontrado ou versão incorreta: " + id);
            }

        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao reativar paciente.", e);
        }
    }
}
