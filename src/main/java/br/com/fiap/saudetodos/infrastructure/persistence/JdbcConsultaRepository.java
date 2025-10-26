package br.com.fiap.saudetodos.infrastructure.persistence;

import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import br.com.fiap.saudetodos.domain.exceptions.EntidadeNaoLocalizada;
import br.com.fiap.saudetodos.domain.model.Consulta;
import br.com.fiap.saudetodos.domain.model.Medico;
import br.com.fiap.saudetodos.domain.model.Paciente;
import br.com.fiap.saudetodos.domain.repository.ConsultaRepository;
import br.com.fiap.saudetodos.domain.repository.PacienteRepository;
import br.com.fiap.saudetodos.infrastructure.exceptions.InfraestruturaException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class JdbcConsultaRepository implements ConsultaRepository {

    private final DatabaseConnection conexaoBD;

    @Inject
    PacienteRepository pacRepo;

    @Inject
    public JdbcConsultaRepository(AgroalDataSource ds) {
        this.conexaoBD = new DatabaseConnectionImpl(ds);
    }

    @Override
    public Consulta salvar(Consulta consulta) {
        String sql = "INSERT INTO CONSULTA "
                + "(data_consulta, hora_consulta, status, paciente_id, medico_id, ativo, version, created_at, last_update) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String[] generatedColumns = { "ID" };

        try (Connection conn = conexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, generatedColumns)) {

            stmt.setDate(1, Date.valueOf(consulta.getData()));
            stmt.setTime(2, Time.valueOf(consulta.getHora()));
            stmt.setString(3, consulta.getStatus());
            stmt.setInt(4, consulta.getPaciente().getId());
            stmt.setInt(5, consulta.getMedico().getId());
            stmt.setInt(6, 1); // Oracle NUMBER(1) usado como booleano
            stmt.setLong(7, consulta.getVersao());

            Timestamp agora = new Timestamp(System.currentTimeMillis());
            stmt.setTimestamp(8, agora);
            stmt.setTimestamp(9, agora);

            if (stmt.executeUpdate() == 0) {
                throw new InfraestruturaException("Falha ao salvar consulta.");
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    consulta.setId(rs.getInt(1));
                }
            }
            return consulta;

        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao salvar consulta.", e);
        }
    }

    @Override
    public Consulta buscarPorId(long id) throws EntidadeNaoLocalizada {
        String sql = "SELECT data_consulta, hora_consulta, status, paciente_id, medico_id "
                + "FROM CONSULTA WHERE id = ? AND ativo = 1";

        try (Connection conn = conexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new EntidadeNaoLocalizada("Consulta não encontrada: " + id);
                }

                LocalDate data      = rs.getDate("data_consulta").toLocalDate();
                LocalTime hora      = rs.getTime("hora_consulta").toLocalTime();
                String status       = rs.getString("status");
                int pid             = rs.getInt("paciente_id");
                int mid             = rs.getInt("medico_id");

                // busca o paciente completo
                Paciente paciente = pacRepo.buscarPorId(pid);

                // ainda não há repositório de Medico: criamos um dummy com nome não-vazio
                Medico medico = new Medico(mid,
                        "Médico#" + mid,
                        "",     // contato
                        "",     // crm
                        ""      // especialidade
                );

                return new Consulta((int) id, data, hora, status, paciente, medico);
            }

        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao buscar consulta.", e);
        }
    }

    @Override
    public void editar(Consulta consulta) throws InfraestruturaException, EntidadeNaoLocalizada {
        String sql = "UPDATE CONSULTA "
                + "SET data_consulta = ?, hora_consulta = ?, status = ?,"
                + "    version = ?, last_update = ? "
                + "WHERE id = ? AND ativo = 1";

        try (Connection conn = conexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            long novaVersao = consulta.getVersao() + 1;
            Timestamp agora = new Timestamp(System.currentTimeMillis());

            stmt.setDate(1, Date.valueOf(consulta.getData()));
            stmt.setTime(2, Time.valueOf(consulta.getHora()));
            stmt.setString(3, consulta.getStatus());
            stmt.setLong(4, novaVersao);
            stmt.setTimestamp(5, agora);
            stmt.setLong(6, consulta.getId());

            if (stmt.executeUpdate() == 0) {
                throw new EntidadeNaoLocalizada("Consulta não encontrada para editar: " + consulta.getId());
            }
            consulta.setVersao(novaVersao);

        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao editar consulta.", e);
        }
    }

    @Override
    public List<Consulta> buscarTodos() {
        // Se precisar, implemente um SELECT + join em PACIENTE etc.
        return new ArrayList<>();
    }

    @Override
    public void finalizar(long id, long versao) throws InfraestruturaException, EntidadeNaoLocalizada {
        String sql = "UPDATE CONSULTA "
                + "SET status = 'FINALIZADA', version = ?, last_update = ? "
                + "WHERE id = ? AND version = ?";

        try (Connection conn = conexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, versao + 1);
            stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            stmt.setLong(3, id);
            stmt.setLong(4, versao);

            if (stmt.executeUpdate() == 0) {
                throw new EntidadeNaoLocalizada("Consulta não encontrada ou versão inválida: " + id);
            }

        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao finalizar consulta.", e);
        }
    }
}
