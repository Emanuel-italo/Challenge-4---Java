package br.com.fiap.saudetodos.domain.model;

import java.util.Scanner;

public abstract class ModuloTelemedicina {
    private String nome;
    private String descricao;

    public ModuloTelemedicina(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }


    public abstract void executar(Scanner scanner);
}
