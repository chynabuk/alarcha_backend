package com.project.alarcha.util;


import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@UtilityClass
public class EmailSender {
    @Autowired
    private JavaMailSender javaMailSender;

    public static void sendEmail(String toEmail, String subject, String body){
        SimpleMailMessage message = new SimpleMailMessage();
        //Нужно будет поменять(также в application.properties прописать username и password
        message.setFrom("sharshenkulov.1@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        javaMailSender.send(message);
    }
}
