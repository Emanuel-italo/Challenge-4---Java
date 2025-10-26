package br.com.fiap.saudetodos.domain.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class AgendaFacil extends ModuloTelemedicina {

    public AgendaFacil() {
        super("AgendaFácil", "Agendamento e lembretes inteligentes");
    }

    @Override
    public void executar(Scanner scanner) {
        System.out.println("----- Executando AgendaFácil -----");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");


        LocalDate consultaDate = null;
        while (consultaDate == null) {
            System.out.print("Digite a data da consulta (DD/MM/YYYY): ");
            String dataStr = scanner.nextLine().trim();
            try {
                consultaDate = LocalDate.parse(dataStr, dateFormatter);

                if (consultaDate.getYear() < 2025) {
                    System.out.println("Por favor, informe uma data a partir de 2025.");
                    consultaDate = null;
                }
            } catch (Exception e) {
                System.out.println("Data inválida! Certifique-se que o dia (01-31), mês (01-12) e o" +
                        " ano estejam corretos.");
            }
        }


        LocalTime consultaTime = null;
        while (consultaTime == null) {
            System.out.print("Digite o horário da consulta (HH:mm): ");
            String horaStr = scanner.nextLine().trim();
            try {
                consultaTime = LocalTime.parse(horaStr, timeFormatter);

                LocalTime inicio = LocalTime.of(8, 0);
                LocalTime fim = LocalTime.of(17, 0);
                if (consultaTime.isBefore(inicio) || consultaTime.isAfter(fim)) {
                    System.out.println("Horário inválido! Por favor, insira um horário entre 08:00 e 17:00.");
                    consultaTime = null; // reinicia a leitura
                }
            } catch (Exception e) {
                System.out.println("Horário inválido! Use o formato HH:mm (exemplo: 14:30).");
            }
        }

        System.out.println("Consulta agendada para " + consultaDate.format(dateFormatter)
                + " às " + consultaTime.format(timeFormatter) + ".");
        System.out.println("O aplicativo lembrará você automaticamente na data agendada.");
        System.out.println("----------------------------------");
    }
}

