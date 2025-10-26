package br.com.fiap.saudetodos.domain.model;


public class Paciente extends Pessoa {
    private int idade;
    private String tipoDeficiencia;
    private String telefone;
    private long versao;

    public Paciente(int id, String nome, int idade, String tipoDeficiencia, String telefone) {
        super(id, nome, telefone);
        setIdade(idade);
        this.tipoDeficiencia = tipoDeficiencia;
        this.telefone = telefone;
        this.versao = 1L;
    }



    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        if (idade <= 0 || idade >= 99) {
            throw new IllegalArgumentException("Idade inválida!");
        }
        if (idade >= 65) {
            System.out.println("Paciente com mais de 65 anos. Recursos de acessibilidade especiais serão ativados.");
        }
        this.idade = idade;
    }

    public String getTipoDeficiencia() {
        return tipoDeficiencia;
    }

    public void setTipoDeficiencia(String tipoDeficiencia) {
        this.tipoDeficiencia = tipoDeficiencia;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public boolean isIdoso() {
        return this.idade >= 65;
    }



    public long getVersao() {
        return versao;
    }

    public void setVersao(long versao) {
        this.versao = versao;
    }




    public int getId() {
        return super.getId();
    }

    public String getNome() {
        return super.getNome();
    }

    @Override
    public String toString() {
        return "Paciente [id=" + getId()
                + ", nome=" + getNome() + ", idade=" + idade
                + ", tipoDeficiencia=" + tipoDeficiencia + ", telefone=" + telefone + ", versao=" + versao + "]";
    }
}
