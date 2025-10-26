package br.com.fiap.saudetodos.infrastructure.main;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

import jakarta.inject.Inject;


@QuarkusMain
public class Main implements QuarkusApplication {

    @Inject
    TesteAplicacao teste;


    public static void main(String... args) {
        System.out.println("Iniciando aplicação Quarkus...");
        Quarkus.run(Main.class, args);
        System.out.println("Aplicação Quarkus finalizada.");
    }


    @Override
    public int run(String... args) {
        System.out.println(">>> Executando TesteAplicacao.executarTestesCRUD() <<<");
        try {
            teste.executarTestesCRUD();
            System.out.println(">>> TesteAplicacao.executarTestesCRUD() finalizado com sucesso. <<<");

            Quarkus.waitForExit();
            return 0;
        } catch (Exception e) {
            System.err.println("!!! ERRO GERAL durante a execução dos testes !!!");
            e.printStackTrace();

            Quarkus.asyncExit(1);
            return 1;
        }
    }
}