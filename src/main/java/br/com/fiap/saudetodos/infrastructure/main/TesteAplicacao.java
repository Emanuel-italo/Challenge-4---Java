package br.com.fiap.saudetodos.infrastructure.main;


import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import br.com.fiap.saudetodos.domain.exceptions.EntidadeNaoLocalizada;
import br.com.fiap.saudetodos.domain.model.Paciente;
import br.com.fiap.saudetodos.domain.model.Consulta;
import br.com.fiap.saudetodos.domain.model.Medico;
import br.com.fiap.saudetodos.domain.repository.PacienteRepository;
import br.com.fiap.saudetodos.domain.repository.ConsultaRepository;
import br.com.fiap.saudetodos.infrastructure.persistence.DatabaseConnection;
import br.com.fiap.saudetodos.infrastructure.persistence.DatabaseConnectionImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@ApplicationScoped
public class TesteAplicacao {

    @Inject
    AgroalDataSource ds;

    @Inject
    PacienteRepository pacRepo;

    @Inject
    ConsultaRepository conRepo;


    public void executarTestesCRUD() {
        System.out.println(">>> INICIANDO PREPARAÇÃO DO ESQUEMA <<<");
        preparaEsquema();
        System.out.println(">>> ESQUEMA PREPARADO <<<");


        System.out.println("\n--- Iniciando Testes CRUD ---");

        try {

            System.out.println("\n--- TESTANDO PACIENTE ---");
            System.out.println("1. Tentando CRIAR paciente...");
            Paciente p1 = new Paciente(0, "Ana Maria", 68, "Visual", "(11)91111-1111", "11122233301", "ana.maria@email.com");
            Paciente p1Salvo = pacRepo.salvar(p1);
            if (p1Salvo != null && p1Salvo.getId() > 0) {
                System.out.println("   -> SUCESSO! Paciente criado com ID: " + p1Salvo.getId());
            } else {
                System.err.println("   -> FALHA ao criar paciente!"); return;
            }
            int idAna = p1Salvo.getId();


            System.out.println("2. Tentando BUSCAR por ID (" + idAna + ")...");
            Paciente pBuscadoId = pacRepo.buscarPorId(idAna);
            System.out.println("   -> SUCESSO! Encontrado: " + pBuscadoId);


            System.out.println("3. Tentando BUSCAR por CPF (11122233301)...");
            Paciente pBuscadoCpf = pacRepo.buscarPorCpf("11122233301");
            System.out.println("   -> SUCESSO! Encontrado: " + pBuscadoCpf);


            System.out.println("4. Tentando EDITAR paciente ID " + idAna + " (mudando telefone)...");
            pBuscadoId.setTelefone("(11)95555-4444"); // Altera o telefone no objeto
            boolean editado = pacRepo.editar(pBuscadoId); // Tenta salvar a alteração
            if (editado) {
                Paciente pEditado = pacRepo.buscarPorId(idAna); // Busca de novo para confirmar
                System.out.println("   -> SUCESSO! Paciente atualizado: " + pEditado);
            } else {
                System.err.println("   -> FALHA ao editar paciente!");
            }


            System.out.println("5. Tentando CRIAR segundo paciente (Beto Lima)...");
            Paciente p2 = new Paciente(0, "Beto Lima", 50, null, "(21)92222-2222", "99988877702", "beto@email.com");
            pacRepo.salvar(p2);


            System.out.println("6. Tentando LISTAR todos pacientes ativos...");
            List<Paciente> todosPacientes = pacRepo.buscarTodos();
            System.out.println("   -> SUCESSO! Encontrados " + todosPacientes.size() + " pacientes:");
            for(Paciente item : todosPacientes) {
                System.out.println("      - " + item);
            }


            System.out.println("\n--- TESTANDO CONSULTA ---");

            Medico m1 = new Medico(501, "Dr. Carlos Silva", "(11)1234-5678", "CRM9876SP", "Clínica Geral");


            System.out.println("7. Tentando AGENDAR consulta para " + pBuscadoId.getNome() + "...");
            Consulta c1 = new Consulta(0,
                    LocalDate.now().plusDays(10), // Daqui a 10 dias
                    LocalTime.of(14, 0), // Às 14:00
                    "AGENDADA",
                    pBuscadoId,
                    m1);
            Consulta c1Salva = conRepo.salvar(c1);
            if (c1Salva != null && c1Salva.getId() > 0) {
                System.out.println("   -> SUCESSO! Consulta agendada com ID: " + c1Salva.getId());
            } else {
                System.err.println("   -> FALHA ao agendar consulta!"); return;
            }
            int idConsulta = c1Salva.getId();


            System.out.println("8. Tentando BUSCAR consulta por ID (" + idConsulta + ")...");
            Consulta cBuscadaId = conRepo.buscarPorId(idConsulta);
            System.out.println("   -> SUCESSO! Encontrada: " + cBuscadaId);


            System.out.println("9. Tentando LISTAR consultas do paciente ID " + idAna + "...");
            List<Consulta> consultasPaciente = conRepo.buscarPorPacienteId(idAna);
            System.out.println("   -> SUCESSO! Encontradas " + consultasPaciente.size() + " consultas:");
            for(Consulta item : consultasPaciente) {
                System.out.println("      - " + item);
            }


            System.out.println("10. Tentando EDITAR consulta ID " + idConsulta + " (status para CONFIRMADA)...");
            cBuscadaId.setStatus("CONFIRMADA");
            boolean consultaEditada = conRepo.editar(cBuscadaId);
            if (consultaEditada) {
                Consulta cEditada = conRepo.buscarPorId(idConsulta);
                System.out.println("   -> SUCESSO! Consulta atualizada: " + cEditada);
            } else {
                System.err.println("   -> FALHA ao editar consulta!");
            }


            System.out.println("11. Tentando CANCELAR consulta ID " + idConsulta + "...");
            boolean cancelada = conRepo.cancelar(idConsulta);
            if (cancelada) {
                System.out.println("   -> SUCESSO! Consulta cancelada.");

                try {
                    conRepo.buscarPorId(idConsulta);
                    System.err.println("      -> ERRO: Consulta cancelada ainda foi encontrada!");
                } catch (EntidadeNaoLocalizada e) {
                    System.out.println("      -> Verificação OK: Consulta cancelada não encontrada.");
                }
            } else {
                System.err.println("   -> FALHA ao cancelar consulta!");
            }



            System.out.println("\n--- TESTANDO DESATIVAR PACIENTE ---");

            System.out.println("12. Tentando DESATIVAR paciente ID " + idAna + "...");
            boolean desativado = pacRepo.desativar(idAna);
            if (desativado) {
                System.out.println("   -> SUCESSO! Paciente desativado.");

                try {
                    pacRepo.buscarPorId(idAna);
                    System.err.println("      -> ERRO: Paciente desativado ainda foi encontrado!");
                } catch (EntidadeNaoLocalizada e) {
                    System.out.println("      -> Verificação OK: Paciente desativado não encontrado.");
                }
            } else {
                System.err.println("   -> FALHA ao desativar paciente!");
            }


        } catch (EntidadeNaoLocalizada e) {

            System.err.println("!!! ERRO DE TESTE: Entidade não localizada inesperadamente !!!");
            System.err.println("    Mensagem: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("!!! ERRO DE TESTE: Uma exceção inesperada ocorreu !!!");
            System.err.println("    Tipo: " + e.getClass().getName());
            System.err.println("    Mensagem: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n--- Testes CRUD Finalizados ---");
    }

    private void preparaEsquema() {

        DatabaseConnection db = new DatabaseConnectionImpl(ds);


        String ddlPaciente =
                "CREATE TABLE PACIENTE (" +
                        " id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                        " nome VARCHAR2(100) NOT NULL, " +
                        " idade NUMBER, " +
                        " tipo_deficiencia VARCHAR2(50), " +
                        " telefone VARCHAR2(20), " +
                        " cpf VARCHAR2(11) NOT NULL UNIQUE, " +
                        " email VARCHAR2(100) UNIQUE, " +
                        " ativo NUMBER(1) DEFAULT 1, " +
                        " created_at TIMESTAMP DEFAULT SYSTIMESTAMP, " +
                        " last_update TIMESTAMP DEFAULT SYSTIMESTAMP " +
                        ")";


        String ddlConsulta =
                "CREATE TABLE CONSULTA (" +
                        " id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                        " data_consulta DATE NOT NULL, " +
                        " hora_consulta TIMESTAMP NOT NULL, " +
                        " status VARCHAR2(20) NOT NULL, " +
                        " paciente_id NUMBER NOT NULL, " +
                        " medico_id NUMBER NOT NULL, " +
                        " ativo NUMBER(1) DEFAULT 1, " +
                        " created_at TIMESTAMP DEFAULT SYSTIMESTAMP, " +
                        " last_update TIMESTAMP DEFAULT SYSTIMESTAMP, " +
                        " CONSTRAINT fk_consulta_paciente FOREIGN KEY (paciente_id) REFERENCES PACIENTE(id) " +
                        // Adicionar FK para medico quando a tabela MEDICO existir
                        // " , CONSTRAINT fk_consulta_medico FOREIGN KEY (medico_id) REFERENCES MEDICO(id) " +
                        ")";


        String ddlMedico =
                "CREATE TABLE MEDICO (" +
                        " id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                        " nome VARCHAR2(100) NOT NULL, " +
                        " crm VARCHAR2(20) NOT NULL UNIQUE, " +
                        " especialidade VARCHAR2(100) NOT NULL, " +
                        " contato VARCHAR2(100) " +
                        ")";


        Connection conn = null;
        Statement stmt = null;

        System.out.println("Preparando esquema do banco (DROP e CREATE tabelas)...");
        try {
            conn = db.getConnection();
            stmt = conn.createStatement();


            System.out.println("   - Tentando DROP TABLE CONSULTA...");
            try { stmt.execute("DROP TABLE CONSULTA CASCADE CONSTRAINTS"); } catch (SQLException e) { System.out.println("     (Não existia ou erro: " + e.getMessage() + ")"); }

            System.out.println("   - Tentando DROP TABLE PACIENTE...");
            try { stmt.execute("DROP TABLE PACIENTE CASCADE CONSTRAINTS"); } catch (SQLException e) { System.out.println("     (Não existia ou erro: " + e.getMessage() + ")"); }

            System.out.println("   - Tentando DROP TABLE MEDICO...");
            try { stmt.execute("DROP TABLE MEDICO CASCADE CONSTRAINTS"); } catch (SQLException e) { System.out.println("     (Não existia ou erro: " + e.getMessage() + ")"); }



            System.out.println("   - Criando TABLE PACIENTE...");
            stmt.execute(ddlPaciente);
            System.out.println("   - Criando TABLE MEDICO...");
            stmt.execute(ddlMedico);
            System.out.println("   - Criando TABLE CONSULTA...");
            stmt.execute(ddlConsulta);


            System.out.println("Esquema preparado com sucesso!");

        } catch (SQLException e) {

            System.err.println("!!! ERRO FATAL ao preparar esquema do banco !!!");
            System.err.println("    SQL State: " + e.getSQLState());
            System.err.println("    Error Code: " + e.getErrorCode());
            System.err.println("    Mensagem: " + e.getMessage());
            e.printStackTrace();

            throw new RuntimeException("Não foi possível criar as tabelas no banco de dados.", e);
        } finally {

            try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
