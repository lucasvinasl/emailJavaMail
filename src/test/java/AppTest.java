import org.junit.Test;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class AppTest {
    /*
        private String userName = "javalucasvinicius@gmail.com";
        // private String password = "M!nh@senh@123";
        private String passwordGoogle = "qehd gajl ksmd jglx";
     */

    private String userName;
    private String passwordGoogle;

    //foi um método elaborado pelo GTP para repassar as credenciais com maior segurança.
    private void loadCredentials(){

        Properties prop = new Properties();
        try{

            prop.load(new FileReader("configProperties/config.properties"));
            userName = prop.getProperty("email.username");
            passwordGoogle = prop.getProperty("email.password");

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testeEmail(){

        loadCredentials();

        if (userName == null || passwordGoogle == null) {
            System.out.println("❌ Credenciais não carregadas. Verifique o arquivo config.properties.");
            return;
        }else{
            System.out.println("✅ Credenciais carregadas com sucesso!");
        }

        try{
            /* Cada domínio de email tem suas configurações SMTP*/
            Properties properties = new Properties();
            //SMTP - é o protocolo de envio de email padrão

            properties.put("mail.smtp.auth", "true"); // Autorização
            properties.put("mail.smtp.starttls.enable", "true"); // Habilita o protocolo TSL
            properties.put("mail.smtp.host", "smtp.gmail.com"); // Servidor SMTP do Gmail
            properties.put("mail.smtp.port", "587"); // Porta do Servidor em TSL
            properties.put("mail.smtp.ssl.trust", "smtp.gmail.com"); // Confiança no servidor Gmail

            //Socket é um ponto de comunicação entre o JavaMail e SMTP
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
            //e-mail de origem e Remetente
            mensagem.setFrom(new InternetAddress(userName, "Lucas - DEV Java"));
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
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }


    }
}
