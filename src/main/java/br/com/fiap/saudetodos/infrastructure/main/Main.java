package br.com.fiap.saudetodos.infrastructure.main;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;



@QuarkusMain
public class Main implements QuarkusApplication {



    public static void main(String... args) {
        System.out.println("Iniciando aplicação Quarkus...");

        Quarkus.run(Main.class, args);
    }

    @Override
    public int run(String... args) throws Exception {
        System.out.println("Aplicação iniciada. Aguardando requisições...");


        Quarkus.waitForExit();

        System.out.println("Aplicação Quarkus sendo finalizada...");
        return 0;
    }
}