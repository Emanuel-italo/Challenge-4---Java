package br.com.fiap.saudetodos.domain.model;

import java.util.Scanner;

public class AjudaGeral extends ModuloTelemedicina {

    public AjudaGeral() {
        super("AjudaGeral", "Central de Ajuda e Suporte para todo o serviço de teleatendimento");
    }

    @Override
    public void executar(Scanner scanner) {
        System.out.println("----- Executando Ajuda e Suporte -----");

        System.out.println("Caio: " +
                "Oi, eu sou o Caio, seu assistente, e tô aqui pra te ajudar com tudo que precisar no nosso sistema!");
        System.out.println();

        System.out.println("Caio: Vou te passar umas dicas de como usar cada parte do serviço, ta bom?");
        System.out.println();

        System.out.println("1. Cadastro e Acesso:");

        System.out.println("   - Quando você preencher seu nome, idade e telefone, tente caprichar, viu? " +
                "Dados errados podem atrapalhar seu atendimento.");

        System.out.println("   - Se rolar alguma dúvida no cadastro, digite 'ajuda' e eu te explico tudo direitinho.");
        System.out.println();

        System.out.println("2. AgendaFácil:");

        System.out.println("   - Esse módulo é pra você marcar a sua consulta." +
                " É só informar a data e o horário que você prefere.");

        System.out.println("   - Leia com calma as instruções pra evitar marcar um horário errado.");
        System.out.println();


        System.out.println("3. FalaConsulta:");

        System.out.println("   - Aqui funciona como um assistente de voz. " +
                "Vou te fazer algumas perguntas sobre sua saúde e seus sintomas.");

        System.out.println("   - Responda com clareza para que seu atendimento seja o mais adequado possível.");
        System.out.println();


        System.out.println("4. FocoCerto:");

        System.out.println("   - Para que a teleconsulta fique com uma boa qualidade, ajuste a " +
                "câmera de modo que seu rosto fique bem centralizado e verifique se o ambiente está bem iluminado.");

        System.out.println("   - Se tiver dificuldade para ajustar, digite 'repetir'" +
                " e eu explico novamente como posicionar direitinho.");

        System.out.println();


        System.out.println("5. RedeCuidar:");
        System.out.println("   - Se você precisar que um cuidador acompanhe a consulta, " +
                "o sistema vai pedir para que ele informe o nome, o CPF e o motivo para acompanhar.");

        System.out.println("   - O motivo pode ser, por exemplo, que o idoso tem dificuldade com o" +
                " celular ou prefere um atendimento mais pessoal.");
        System.out.println();

        System.out.println("Caio: Sempre que você se sentir inseguro ou tiver qualquer dúvida, " +
                "é só digitar 'ajuda' e eu apareço pra orientar você.");
        System.out.println();


        System.out.println("Caio: Espero que essas dicas ajudem você a usar nosso serviço com mais confiança. " +
                "Nossa prioridade é que você se sinta seguro e bem cuidado.");

        System.out.println("Se precisar de um atendimento mais direto, procure a opção de suporte no " +
                "menu ou ligue para o nosso número de atendimento.");


        System.out.println("----- Fim da Ajuda e Suporte -----");
    }
}
