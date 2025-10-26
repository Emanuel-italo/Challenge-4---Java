package br.com.fiap.saudetodos.domain.model;


import java.util.Scanner;

public class RedeCuidar extends ModuloTelemedicina {

    public RedeCuidar() {
        super("RedeCuidar", "Suporte para cuidadores/familiares");
    }

    @Override
    public void executar(Scanner scanner) {
        System.out.println("----- Executando RedeCuidar -----");
        System.out.print("Caio: Deseja confirmar o acompanhamento remoto? (Sim/Não): ");
        String resp = scanner.nextLine().trim();

        if (resp.equalsIgnoreCase("Sim")) {
            System.out.print("Caio: Por favor, informe seu nome: ");
            String nomeAcompanhante = scanner.nextLine();


            String cpfAcompanhante = "";
            do {
                System.out.print("Caio: Agora, informe seu CPF (somente números, 11 dígitos): ");
                cpfAcompanhante = scanner.nextLine().trim();
                if (cpfAcompanhante.length() != 11) {
                    System.out.println("Caio: CPF inválido. Ele deve conter exatamente 11 dígitos. Tente novamente.");
                }
            } while (cpfAcompanhante.length() != 11);

            System.out.println("Caio: Para que motivo você deseja acompanhar a consulta?");
            System.out.println(" 1 - O idoso tem dificuldade em mexer no celular.");
            System.out.println(" 2 - O idoso prefere interação pessoal, pois não se adapta bem ao " +
                    "atendimento digital.");

            System.out.println(" 3 - Necessidade de supervisão constante por segurança.");
            System.out.println(" 4 - Outro motivo.");
            System.out.print("Caio: Escolha uma opção (1-4): ");

            int opcaoMotivo = 0;
            try {
                opcaoMotivo = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                opcaoMotivo = 4;
            }

            String motivo = "";
            switch (opcaoMotivo) {
                case 1:
                    motivo = "O idoso tem dificuldade em mexer no celular.";
                    break;
                case 2:
                    motivo = "O idoso prefere interação pessoal, pois não se adapta bem ao atendimento digital.";
                    break;
                case 3:
                    motivo = "Necessidade de supervisão constante por segurança.";
                    break;
                case 4:
                default:
                    System.out.print("Caio: Por favor, descreva o motivo: ");
                    motivo = scanner.nextLine();
                    break;
            }

            System.out.println("Caio: Acompanhamento confirmado. O cuidador " + nomeAcompanhante
                    + " (CPF: " + cpfAcompanhante + ") será notificado. Motivo: " + motivo);
        } else {
            System.out.println("Caio: Acompanhamento não confirmado.");
        }
        System.out.println("---------------------------------");
    }
}


