package com.edmanager.util;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtil {

    private static String MAILER_EMAIL_ADDRESS = "mailer.edm@gmail.com";
    private static String MAILER_EMAIL_PASSWORD = "iamedm420";

    public static  boolean send(String to,String sub,String msg,String name) {

            try {
                //Get properties object
                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.socketFactory.port", "465");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", "587");
                props.put("mail.smtp.starttls.enable","true");
                props.put("mail.smtp.ssl.trust","*");

                //get Session
                Session session = Session.getInstance(props,new javax.mail.Authenticator() {
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new javax.mail.PasswordAuthentication(MAILER_EMAIL_ADDRESS,MAILER_EMAIL_PASSWORD);
                    }
                });

                //compose message
                MimeMessage message = new MimeMessage(session);
                message.addRecipient(javax.mail.Message.RecipientType.TO,new InternetAddress(to));
                message.setFrom(new InternetAddress(MAILER_EMAIL_ADDRESS, name));
                message.setSubject(sub);
                message.setText(msg);

                //send message
                javax.mail.Transport.send(message);
                return true;
            } catch (Exception ex) {
                return false;
            }
    }
}
