package br.com.fiap.saudetodos.domain.model;

import java.util.List;

public class Hospital {
    private String nome;
    private int numLeitos;
    private List<Instituto> institutos;

    public Hospital(String nome, int numLeitos, List<Instituto> institutos) {
        this.nome = nome;
        this.numLeitos = numLeitos;
        this.institutos = institutos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getNumLeitos() {
        return numLeitos;
    }

    public void setNumLeitos(int numLeitos) {
        this.numLeitos = numLeitos;
    }

    public List<Instituto> getInstitutos() {
        return institutos;
    }

    public void setInstitutos(List<Instituto> institutos) {
        this.institutos = institutos;
    }

    @Override
    public String toString() {
        String lista = (institutos != null ? institutos.toString() : "[]");
        return "Hospital [nome=" + nome + ", numLeitos=" + numLeitos + ", institutos=" + lista + "]";
    }
}
