package br.com.fiap.saudetodos.domain.model;

import java.util.Scanner;

public class FocoCerto extends ModuloTelemedicina {

    public FocoCerto() {
        super("FocoCerto", "Guia para posicionar a câmera corretamente");
    }

    @Override
    public void executar(Scanner scanner) {
        System.out.println("----- Executando FocoCerto -----");

        int simCount = 0;


        System.out.println("Caio: Por favor, dê uma olhada na sua câmera. Seu rosto está bem enquadrado?");
        System.out.print("Caio: Responda (Sim/Não): ");
        String respostaEnquadramento = scanner.nextLine().trim();
        if (respostaEnquadramento.equalsIgnoreCase("Sim")) {
            simCount++;
            System.out.println("Caio: Ótimo, seu enquadramento parece estar correto.");
        } else {
            System.out.println("Caio: Entendi, parece que seu rosto não está bem enquadrado. Por favor, ajuste a " +
                    "posição da câmera para centralizar melhor seu rosto.");
        }


        System.out.println("Caio: Agora, verifique: você centralizou seu rosto na imagem?");
        System.out.print("Caio: Responda (Sim/Não): ");
        String respostaCentralizado = scanner.nextLine().trim();
        if (respostaCentralizado.equalsIgnoreCase("Sim")) {
            simCount++;
            System.out.println("Caio: Muito bem, seu rosto está centralizado.");
        } else {
            System.out.println("Caio: Por favor, ajuste para que seu rosto fique bem no centro da imagem.");
        }


        System.out.println("Caio: E quanto à iluminação, o ambiente onde você está é bem iluminado?");
        System.out.print("Caio: Responda (Sim/Não): ");
        String respostaIluminacao = scanner.nextLine().trim();
        if (respostaIluminacao.equalsIgnoreCase("Sim")) {
            simCount++;
            System.out.println("Caio: Ótimo, a iluminação parece estar boa.");
        } else {
            System.out.println("Caio: Tente se posicionar em um local com mais luz ou ajustar " +
                    "a iluminação para melhorar a imagem.");
        }


        if (simCount == 3) {
            System.out.println("Caio: Apesar de você ter respondido 'Sim' para tudo, às vezes mesmo quando " +
                    "parece certo pode haver pequenos ajustes.");
            System.out.println("      -> Verifique se o seu rosto está realmente centralizado, sem cortes" +
                    " ou distorções.");
            System.out.println("      -> Observe a iluminação é essencial.");
            System.out.println("      -> Se possível, faça um teste rápido antes de iniciar a consulta-online.");
        }

        System.out.println("Caio: Obrigado por confirmar os ajustes. Vamos continuar com o atendimento.");
        System.out.println("--------------------------------");
    }
}

