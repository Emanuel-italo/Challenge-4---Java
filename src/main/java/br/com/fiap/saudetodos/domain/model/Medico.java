package br.com.fiap.saudetodos.domain.model;

public class Medico extends br.com.fiap.saudetodos.domain.model.Pessoa {
    private String crm;
    private String especialidade;

    public Medico(int id, String nome, String contato, String crm, String especialidade) {
        super(id, nome, contato);
        this.crm = crm;
        this.especialidade = especialidade;
    }

    public String getCrm() {
        return crm;
    }
    public void setCrm(String crm) {
        this.crm = crm;
    }
    public String getEspecialidade() {
        return especialidade;
    }
    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    @Override
    public String toString() {
        return "Medico [" + super.toString() + ", crm=" + crm + ", especialidade=" + especialidade + "]";
    }
}

