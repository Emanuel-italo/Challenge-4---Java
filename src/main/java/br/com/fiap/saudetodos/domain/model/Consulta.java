package br.com.fiap.saudetodos.domain.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Consulta {
    private int id;
    private LocalDate data;
    private LocalTime hora;
    private String status;
    private Paciente paciente;
    private Medico medico;
    private long versao;

    public Consulta(int id,
                    LocalDate data,
                    LocalTime hora,
                    String status,
                    Paciente paciente,
                    Medico medico) {
        this.id = id;
        this.data = data;
        this.hora = hora;
        this.status = status;
        this.paciente = paciente;
        this.medico = medico;
        this.versao = 1L;  // versão inicial sempre começa em 1
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }


    public long getVersao() {
        return versao;
    }

    public void setVersao(long versao) {
        this.versao = versao;
    }



    public boolean isAgendadaParaHoje() {
        return LocalDate.now().equals(this.data);
    }

    public boolean isPeriodoManha() {
        return this.hora.isBefore(LocalTime.NOON);
    }

    @Override
    public String toString() {
        String nomePaciente = paciente != null ? paciente.getNome() : "null";
        String nomeMedico   = medico   != null ? medico.getNome()   : "null";
        return "Consulta [id=" + id + ", data=" + data + ", hora=" + hora
                + ", status=" + status + ", paciente=" + nomePaciente + ", medico=" + nomeMedico + ", versao=" + versao + "]";
    }
}
