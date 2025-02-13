package com.br.emailJavaMail;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class sendMail {

    private String userName;
    private String password;
    //private String destinatarios;
    private List<String> destinatarios = new ArrayList<>();
    private String remetente;
    private String assuntoEmail;
    private String corpoEmail;
    private List<String> pathAnexo;

    // Construtor com HTML
    public sendMail(List<String> destinatarios, String remetente, String assuntoEmail, List<String> pathAnexo){
        this.destinatarios = destinatarios;
        this.remetente = remetente;
        this.assuntoEmail = assuntoEmail;
        this.pathAnexo = pathAnexo;
    }

    // Construtor sem HTML
    public sendMail(List<String> destinatarios, String remetente, String assuntoEmail, String corpoEmail, List<String> pathAnexo){
        this.destinatarios = destinatarios;
        this.remetente = remetente;
        this.assuntoEmail = assuntoEmail;
        this.corpoEmail = corpoEmail;
        this.pathAnexo = pathAnexo;
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

            //Corpo do email com HTML
            StringBuilder mensagemHTML = new StringBuilder();
            mensagemHTML.append("Mensagem Padrão - Java Mail Teste </br></br>");
            mensagemHTML.append("<h2> Você está recebendo uma mensagem teste do Lucas </h2> </br></br>");
            mensagemHTML.append("<a href=\"https://www.instagram.com/lagom.eng/\">Clique aqui.</a>\n");

            String stringHTML = mensagemHTML.toString();

            MimeBodyPart corpoMensagem = new MimeBodyPart();
            if(opcaoHtml){
                corpoMensagem.setContent(stringHTML, "text/html; charset=utf-8");
            }else {
                corpoMensagem.setText(this.corpoEmail);
            }

            // Criando o Anexo
            if(!this.pathAnexo.isEmpty()){
                List<MimeBodyPart> anexos = new ArrayList<>();
                String caminhoAnexo;

                for(int i = 0; i < pathAnexo.size(); i++){
                    caminhoAnexo = pathAnexo.get(i).replace("\"", "");
                    MimeBodyPart anexo = new MimeBodyPart();
                    javax.activation.DataSource source = new javax.activation.FileDataSource(caminhoAnexo);
                    anexo.setDataHandler(new DataHandler(source));
                    //anexo.setFileName(caminhoAnexo.substring(caminhoAnexo.lastIndexOf("\\")+1));
                    anexo.setFileName(new java.io.File(caminhoAnexo).getName());
                    anexos.add(anexo);
                }

                /*
                FileDataSource: indica ao programa onde o arquivo está localizado no sistema.
                DataHandler: fornece os dados do arquivo para o anexo.
                Precisei instalar dependecias para isso.
                 */

                Multipart mp = new MimeMultipart();
                mp.addBodyPart(corpoMensagem);
                for(int i = 0; i < anexos.size(); i++){
                    mp.addBodyPart(anexos.get(i));
                }
                mensagem.setContent(mp);
                System.out.println("Anexo Criado!");
                /*
                Multipart mp = new MimeMultipart(); → Cria um "conteiner" para múltiplas partes.
                addBodyPart(corpoMensagem) → Adiciona o corpo do e-mail.
                addBodyPart(anexo) → Adiciona o anexo ao e-mail.
                mensagem.setContent(mp) → Define o conteúdo final da mensagem como essa estrutura Multipart.
                 */
            }else {
                System.out.println("E-mail sem Anexo.");
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
