package br.com.fiap.saudetodos.domain.model;


import java.util.Scanner;

public class FalaConsulta extends ModuloTelemedicina {

    public FalaConsulta() {
        super("FalaConsulta", "Assistente de voz — acelere seu" +
                " atendimento com o médico enviando um relatório completo do que você sente.");
    }

    @Override
    public void executar(Scanner scanner) {
        System.out.println("----- Executando FalaConsulta -----");
        System.out.println("Caio: Olá! Sou seu assistente virtual para facilitar sua consulta.");
        System.out.print("Confirme que deseja iniciar sua pré-consulta (Sim/Não): ");
        String confirmacao = scanner.nextLine();
        if (!confirmacao.equalsIgnoreCase("Sim")) {
            System.out.println("Caio: Pré-consulta cancelada. Quando precisar, estarei aqui para te ajudar.");
            return;
        }
        System.out.println("Caio: Ótimo! Vamos agilizar seu atendimento com o médico.");
        System.out.println("-----------------------------------");


        System.out.println("Caio: Para facilitar seu atendimento, me diga onde você sente desconforto:");
        System.out.println(" 1) Braço");
        System.out.println(" 2) Perna");
        System.out.println(" 3) Abdômen");
        System.out.println(" 4) Cabeça");
        System.out.println(" 5) Outro tipo de problema");
        System.out.print("Escolha uma opção (1-5): ");
        int opcaoQueixa = 0;
        try {
            opcaoQueixa = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Caio: Entrada inválida, vamos registrar como 'Outro tipo de problema'.");
            opcaoQueixa = 5;
        }

        String localProblema = "";
        switch (opcaoQueixa) {
            case 1:
                localProblema = "Braço";
                break;
            case 2:
                localProblema = "Perna";
                break;
            case 3:
                localProblema = "Abdômen";
                break;
            case 4:
                localProblema = "Cabeça";
                break;
            case 5:
            default:
                System.out.print("Caio: Descreva rapidamente qual seu problema: ");
                localProblema = scanner.nextLine();
                break;
        }

        System.out.println("Caio: Entendi, você relatou um problema no(a) " + localProblema + ".");


        System.out.println("Caio: Pode me informar qual tipo de desconforto você sente?");
        System.out.println(" 1) Queimadura");
        System.out.println(" 2) Dor");
        System.out.println(" 3) Inchaço");
        System.out.println(" 4) Outro tipo de sensação");
        System.out.print("Escolha uma opção (1-4): ");
        int opcaoTipo = 0;
        try {
            opcaoTipo = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Caio: Entrada inválida, vamos registrar como 'Outro tipo de sensação'.");
            opcaoTipo = 4;
        }

        String tipoProblema = "";
        switch (opcaoTipo) {
            case 1:
                tipoProblema = "Queimadura";
                break;
            case 2:
                tipoProblema = "Dor";
                break;
            case 3:
                tipoProblema = "Inchaço";
                break;
            case 4:
            default:
                System.out.print("Caio: Descreva melhor essa sensação: ");
                tipoProblema = scanner.nextLine();
                break;
        }

        System.out.println("Caio: Entendi, você relatou " + tipoProblema + " no(a) " + localProblema + ".");


        System.out.print("Caio: Agora me diga, em uma escala de 1 a 10, o quão desconfortável está essa situação? ");
        int escalaIncomodo = 0;
        try {
            escalaIncomodo = Integer.parseInt(scanner.nextLine());
            if (escalaIncomodo < 1 || escalaIncomodo > 10) {
                System.out.println("Caio: Por favor, informe um valor entre 1 e 10. Vou considerar 5 como padrão.");
                escalaIncomodo = 5;
            }
        } catch (NumberFormatException e) {
            System.out.println("Caio: Entrada inválida, vou assumir um nível médio de 5.");
            escalaIncomodo = 5;
        }

        System.out.println("Caio: Perfeito, nível de desconforto registrado: " + escalaIncomodo + " de 10.");


        System.out.println("\nCaio: Obrigado por compartilhar essas informações.");
        System.out.println("Caio: Vou montar um relatório completo com:");
        System.out.println(" - Local do desconforto: " + localProblema);
        System.out.println(" - Tipo de sensação: " + tipoProblema);
        System.out.println(" - Nível de desconforto: " + escalaIncomodo + " de 10");
        System.out.println("Caio: Isso ajudará o médico a te atender rapidamente e evitar perguntas repetitivas.");
        System.out.println("Caio: Vou encaminhar seu relatório para o médico agora.");
        System.out.println("-----------------------------------");
    }
}

