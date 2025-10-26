package br.com.fiap.saudetodos.domain.model;

import java.time.LocalDateTime;

public class Notificacao {
    private int id;
    private String mensagem;
    private LocalDateTime dataEnvio;
    private Paciente destinatario;

    public Notificacao(int id, String mensagem, LocalDateTime dataEnvio, Paciente destinatario) {
        this.id = id;
        this.mensagem = mensagem;
        this.dataEnvio = dataEnvio;
        this.destinatario = destinatario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(LocalDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public Paciente getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Paciente destinatario) {
        this.destinatario = destinatario;
    }

    public void enviarNotificacao() {
        System.out.println("Enviando notificação para "
                + destinatario.getNome() + ": " + mensagem);
        enviarSMS();
    }

    public void enviarSMS() {
        String telefone = destinatario.getTelefone();
        if (telefone == null || telefone.isEmpty()) {
            System.out.println("Número de telefone não informado. SMS não enviado.");
        } else {
            System.out.println("SMS enviado para o número: "
                    + telefone + " com a mensagem: " + mensagem);
        }
    }


    public boolean wasSentBeforeConsulta(Consulta consulta) {
        return dataEnvio.isBefore(
                LocalDateTime.of(consulta.getData(), consulta.getHora())
        );
    }

    @Override
    public String toString() {
        return "Notificacao [id=" + id
                + ", mensagem=" + mensagem + ", dataEnvio=" + dataEnvio + ", destinatario=" + destinatario.getNome() + "]";
    }
}
