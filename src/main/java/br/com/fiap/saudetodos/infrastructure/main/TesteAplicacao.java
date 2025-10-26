package br.com.fiap.saudetodos.infrastructure.main;

import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;

import br.com.fiap.saudetodos.domain.exceptions.EntidadeNaoLocalizada;
import br.com.fiap.saudetodos.infrastructure.exceptions.InfraestruturaException;
import br.com.fiap.saudetodos.domain.model.Paciente;
import br.com.fiap.saudetodos.domain.model.Consulta;
import br.com.fiap.saudetodos.domain.model.Medico;
import br.com.fiap.saudetodos.domain.repository.PacienteRepository;
import br.com.fiap.saudetodos.domain.repository.ConsultaRepository;
import br.com.fiap.saudetodos.infrastructure.persistence.DatabaseConnectionImpl;

@ApplicationScoped
public class TesteAplicacao {

    @Inject
    AgroalDataSource ds;

    @Inject
    PacienteRepository pacRepo;

    @Inject
    ConsultaRepository conRepo;

    public void executarTudo() {
        preparaEsquema();

        try {

            Paciente p = pacRepo.salvar(
                    new Paciente(0, "Ana Silva", 28, "Nenhuma", "99999-0000")
            );
            System.out.println("CREATE Paciente → " + p);


            p = pacRepo.buscarPorId(p.getId());
            System.out.println("READ   Paciente → " + p);


            p.setTelefone("88888-1111");
            pacRepo.editar(p);
            System.out.println("UPDATE Paciente → " + pacRepo.buscarPorId(p.getId()));




            Medico m = new Medico(1, "Dr. João", "11111-2222", "CRM123", "Cardio");
            Consulta c = conRepo.salvar(
                    new Consulta(
                            0,
                            LocalDate.now(),
                            LocalTime.of(14, 30),
                            "AGENDADA",
                            p,
                            m
                    )
            );
            System.out.println("CREATE Consulta → " + c);


            c = conRepo.buscarPorId(c.getId());
            System.out.println("READ   Consulta → " + c);


            c.setStatus("CONFIRMADA");
            conRepo.editar(c);
            System.out.println("UPDATE Consulta → " + conRepo.buscarPorId(c.getId()));


            conRepo.finalizar(c.getId(), c.getVersao());
            System.out.println("DELETE Consulta → finalizada");


            pacRepo.desativar(p.getId(), p.getVersao());
            System.out.println("DELETE Paciente → inativo");

        } catch (EntidadeNaoLocalizada | InfraestruturaException e) {
            System.err.println("Falha no fluxo de teste: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void preparaEsquema() {
        var db = new DatabaseConnectionImpl(ds);

        String ddlPaciente =
                "CREATE TABLE PACIENTE (" +
                        " id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                        " nome VARCHAR2(100), " +
                        " contato VARCHAR2(100), " +
                        " idade NUMBER, " +
                        " tipo_deficiencia VARCHAR2(50), " +
                        " telefone VARCHAR2(20), " +
                        " ativo NUMBER(1) DEFAULT 1, " +
                        " version NUMBER DEFAULT 1, " +
                        " created_at TIMESTAMP, " +
                        " last_update TIMESTAMP" +
                        ")";

        String ddlConsulta =
                "CREATE TABLE CONSULTA (" +
                        " id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                        " data_consulta DATE, " +
                        " hora_consulta DATE, " +
                        " status VARCHAR2(20), " +
                        " paciente_id NUMBER, " +
                        " medico_id NUMBER, " +
                        " ativo NUMBER(1) DEFAULT 1, " +
                        " version NUMBER DEFAULT 1, " +
                        " created_at TIMESTAMP, " +
                        " last_update TIMESTAMP" +
                        ")";

        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement()) {

            try { stmt.execute("DROP TABLE CONSULTA CASCADE CONSTRAINTS"); } catch (SQLException ignored) {}
            try { stmt.execute("DROP TABLE PACIENTE CASCADE CONSTRAINTS"); } catch (SQLException ignored) {}

            stmt.execute(ddlPaciente);
            stmt.execute(ddlConsulta);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar esquema de teste", e);
        }
    }

}
