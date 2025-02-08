package com.br.emailJavaMail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class sendMail {

    private String userName;
    private String password;
    //private String destinatarios;
    private List<String> destinatarios = new ArrayList<>();
    private String remetente;
    private String assuntoEmail;
    private String corpoEmail;

    public sendMail(List<String> destinatarios, String remetente, String assuntoEmail){
        this.destinatarios = destinatarios;
        this.remetente = remetente;
        this.assuntoEmail = assuntoEmail;
    }

    public sendMail(List<String> destinatarios, String remetente, String assuntoEmail, String corpoEmail){
        this.destinatarios = destinatarios;
        this.remetente = remetente;
        this.assuntoEmail = assuntoEmail;
        this.corpoEmail = corpoEmail;
    }

    public void carregaCredenciais() throws FileNotFoundException {
        Properties prop = new Properties();

        try{
            prop.load(new FileReader("configProperties/config.properties"));
            userName = prop.getProperty("email.username");
            password = prop.getProperty("email.password");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void enviaEmail(boolean opcaoHtml) throws FileNotFoundException {

        carregaCredenciais();


        if(userName == null || password == null){
            System.out.println("❌ Credenciais não carregadas. Verifique o arquivo config.properties.");
            return;
        }else{
            System.out.println("✅ Credenciais carregadas com sucesso!");
        }


        try{
            Properties properties = new Properties();

            properties.put("mail.smtp.auth", "true"); // Autorização
            properties.put("mail.smtp.starttls.enable", "true"); // Habilita o protocolo TSL
            properties.put("mail.smtp.host", "smtp.gmail.com"); // Servidor SMTP do Gmail
            properties.put("mail.smtp.port", "587"); // Porta do Servidor em TSL
            properties.put("mail.smtp.ssl.trust", "smtp.gmail.com"); // Confiança no servidor Gmail


            Session session = Session.getInstance(properties, new Authenticator(){
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userName, password);
                }
            });

            //Transformando o Array em uma String delimitada por "," para jogar no Addres.
            String listaDestinatarios = String.join(",", destinatarios);

            // Lista de endereços destinos
            Address[]  toUser = InternetAddress.parse(listaDestinatarios);
            //Criando um objeto de mensagem conectado a sessão
            Message mensagem = new MimeMessage(session);
            //e-mail de origem e Remetente
            mensagem.setFrom(new InternetAddress(userName, this.remetente));
            //destinatários
            mensagem.setRecipients(Message.RecipientType.TO, toUser);
            //Assunto do email
            mensagem.setSubject(this.assuntoEmail);
            //Corpo do email
            StringBuilder mensagemHTML = new StringBuilder();
            mensagemHTML.append("Mensagem Padrão - Java Mail Teste </br></br>");
            mensagemHTML.append("<h2> Você está recebendo uma mensagem teste do Lucas </h2> </br></br>");
            mensagemHTML.append("<a href=\"https://www.instagram.com/lagom.eng/\">Clique aqui.</a>\n");

            String stringHTML = mensagemHTML.toString();

            if(opcaoHtml){
                mensagem.setContent(stringHTML, "text/html; charset=utf-8");
            }else {
                mensagem.setText(this.corpoEmail);
            }

            // Enviar a mensagem
            Transport.send(mensagem);
            System.out.println("✅ E-mail enviado com sucesso!");

        } catch (MessagingException e) {
            System.out.println("❌ Falha ao enviar o Email");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }


    }

}
