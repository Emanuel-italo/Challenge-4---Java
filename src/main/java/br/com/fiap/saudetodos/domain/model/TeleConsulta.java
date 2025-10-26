package br.com.fiap.saudetodos.domain.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class TeleConsulta extends Consulta {
    private int duracaoMin;
    private boolean orientacaoCorreta;
    private int qualidadeVideo;

    public TeleConsulta(int id, LocalDate data, LocalTime hora, String status,
                        Paciente paciente, Medico medico,
                        int duracaoMin, boolean orientacaoCorreta, int qualidadeVideo) {
        super(id, data, hora, status, paciente, medico);
        setDuracaoMin(duracaoMin);
        this.orientacaoCorreta = orientacaoCorreta;
        setQualidadeVideo(qualidadeVideo);
    }

    public int getDuracaoMin() {
        return duracaoMin;
    }

    public void setDuracaoMin(int duracaoMin) {
        if (duracaoMin < 20 || duracaoMin > 30) {
            throw new IllegalArgumentException("A duração deve ser entre 20 e 30 minutos.");
        }
        this.duracaoMin = duracaoMin;
    }

    public boolean isOrientacaoCorreta() {
        return orientacaoCorreta;
    }

    public void setOrientacaoCorreta(boolean orientacaoCorreta) {
        this.orientacaoCorreta = orientacaoCorreta;
    }

    public int getQualidadeVideo() {
        return qualidadeVideo;
    }

    public void setQualidadeVideo(int qualidadeVideo) {
        if (qualidadeVideo < 0 || qualidadeVideo > 10) {
            throw new IllegalArgumentException("Qualidade de vídeo deve estar entre 0 e 10.");
        }
        this.qualidadeVideo = qualidadeVideo;
    }


    public boolean isQualidadeAdequada() {
        return orientacaoCorreta && qualidadeVideo >= 5;
    }

    @Override
    public String toString() {
        return "Teleconsulta [" + super.toString() + ", duracaoMin=" + duracaoMin +
                ", orientacaoCorreta=" + orientacaoCorreta + ", qualidadeVideo=" + qualidadeVideo + "]";
    }
}
