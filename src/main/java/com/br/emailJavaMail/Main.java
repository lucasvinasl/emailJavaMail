package com.br.emailJavaMail;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        Scanner scan = new Scanner(System.in);

        List<String> listaDestinatarios = new ArrayList<>();


        boolean inputEmails = true;
        boolean validacaoDestinatarioPrincipal = true;
        System.out.println("----- Sistema de Envio de E-mails -----");

        do{

            System.out.println("Informe o Destinatário: ");
            String destinatario = scan.nextLine();

            Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
            Matcher matcher = emailPattern.matcher(destinatario);

            if(matcher.find()){
                System.out.println("✅ - Email Validado");
                listaDestinatarios.add(destinatario);
                validacaoDestinatarioPrincipal = false;
            }else{
                System.out.println("❌ - E-mail Inválido. Reinicie o processo");
            }
            System.out.println("-----------------");

        }while(validacaoDestinatarioPrincipal);


        do{
            System.out.println("Informe outro destinatário, se necessário.");
            System.out.println("Caso não precise mais, Digite 0.");
            String outroDestinatario = scan.nextLine();

            Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
            Matcher matcher = emailPattern.matcher(outroDestinatario);

            if(outroDestinatario.equals("0")){
                inputEmails = false;
                System.out.println("Parando de adicionar destinatários...");
                System.out.println("-----------------");
            }else if(matcher.find()) {
                System.out.println("✅ - Email Validado");
                listaDestinatarios.add(outroDestinatario);
                System.out.println("-----------------");
            }

        }while(inputEmails);


        System.out.println("Informe o Nome do Remetente: ");
        String remetente = scan.nextLine();

        System.out.println("Informe o Título do E-mail: ");
        String assunto = scan.nextLine();

        /*
        System.out.println("Digite a mensagem do E-mail: ");
        String mensagem = scan.nextLine();
         */
        System.out.println("Adicione algum anexo: ");
        String pathAnexo = scan.nextLine();

        System.out.println("1 - Mensagem HTML / 2 - Mensagem de Texto: ");
        String opcaomensagem = scan.nextLine();

        if(opcaomensagem.equals("1")){
            sendMail email = new sendMail(listaDestinatarios,remetente,assunto,pathAnexo);
            email.enviaEmail(true);
        }else{
            System.out.println("Digite a mensagem do E-mail: ");
            String mensagem = scan.nextLine();
            sendMail email = new sendMail(listaDestinatarios,remetente,assunto,mensagem,pathAnexo);
            email.enviaEmail(false);
        }

    }
}