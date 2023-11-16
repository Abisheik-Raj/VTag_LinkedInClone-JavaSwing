package mailClasses;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMailUtil {
    public static void sendMail(String recipient,String content) {
        System.out.println("Preparing to send Email!");
        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        String myAccountEmail = "emailid";
        String password = "password";

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(myAccountEmail, password);
            }
        });

        try {
            Message message = prepareMessage(session, myAccountEmail, recipient,content);
            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Message prepareMessage(Session session, String myAccountEmail, String recipient,String content) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("");
            message.setText(content);
            return message;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

