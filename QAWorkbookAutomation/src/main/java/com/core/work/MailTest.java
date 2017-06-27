package com.core.work;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * Created by SunilDeP on 6/22/2017.
 */
public class MailTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MessagingException {
        String host = "smtp.gmail.com";
        String to = "parikshitd92@gmail.com";
        String from = "parikshitd92@gmail.com";
        String subject = "test";
        String messageText = "body test";

        Properties props = System.getProperties();
        props.put("mail.host", host);
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", "25");

        // If using authentication, otherwise comment out
        props.put("mail.smtp.auth", "true");

        // Gmail requires TLS, your server may not
        props.put("mail.smtp.starttls.enable", "true");

        Session mailSession = Session.getDefaultInstance(props, null);

        Message msg = new MimeMessage(mailSession);
        msg.setFrom(new InternetAddress(from));
        InternetAddress[] address = {new InternetAddress(to)};
        msg.setRecipients(Message.RecipientType.TO, address);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        msg.setText(messageText);

        Transport transport = mailSession.getTransport("smtp");

        //connect with authentication
        //transport.connect(host,"myUsername" , "myPassword");

        //connect without authentication
        transport.connect();
        transport.sendMessage(msg, address);

        transport.close();

        System.out.println("Mail was sent to " + to);
        System.out.println(" from " + from);
        System.out.println(" using host " + host + ".");

    }
}