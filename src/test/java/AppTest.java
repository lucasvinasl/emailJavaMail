import org.junit.Test;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.util.Properties;

public class AppTest {
    /*
        private String userName = "javalucasvinicius@gmail.com";
        // private String password = "M!nh@senh@123";
        private String passwordGoogle = "qehd gajl ksmd jglx";
     */

    private String userName;
    private String passwordGoogle;

    private void loadCredentials(){

        Properties prop = new Properties();
        try{

            prop.load(new FileReader("config.properties"));
            userName = prop.getProperty("email.username");
            passwordGoogle = prop.getProperty("email.password");

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testeEmail(){

        try{
            /* Cada domínio de email tem suas configurações SMTP*/
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true"); // Autorização
            properties.put("mail.smtp.starttls", "true"); // Autenticação
            properties.put("mail.smtp.host", "smtp.gmail.com"); // Servidor gmail
            properties.put("mail.smtp.port", "465"); // Porta do Servidor
            properties.put("mail.smtp.socketFactory.port", "465"); // Especifica a porta Socket
            properties.put("mail.smtp.socketFactory.class" , "javax.net.ssl.SSLSocketFactory"); // Classe Socket

            Session session = Session.getInstance(properties, new Authenticator(){
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userName, passwordGoogle);
                }
            });

            //System.out.println(session); // confirmando a conexão
            // Lista de endereços destinos
            Address[]  toUser = InternetAddress.parse("lagom.ctec@gmail.com");
            //Criando um objeto de mensagem conectado a sessão
            Message mensagem = new MimeMessage(session);
            //e-mail de origem
            mensagem.setFrom(new InternetAddress(userName));
            //destinatários
            mensagem.setRecipients(Message.RecipientType.TO, toUser);
            //Assunto do email
            mensagem.setSubject("E-MAIL em JAVA");
            //Corpo do email
            mensagem.setText("Esta e-mail é um teste e está sendo enviado utilizando Java-Mail !✅");
            // Enviar a mensagem
            Transport.send(mensagem);
            System.out.println("✅ E-mail enviado com sucesso!");
        } catch (MessagingException e) {
            System.out.println("❌ Falha ao enviar o Email");
            e.printStackTrace();
        }


    }
}
