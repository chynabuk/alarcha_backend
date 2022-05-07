package com.project.alarcha.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailSender {
    @Autowired
    private JavaMailSender javaMailSender;

    @EventListener(ApplicationReadyEvent.class)
    public void sendEmail(String toEmail, String subject, String body){
        SimpleMailMessage message = new SimpleMailMessage();
        //Нужно будет поменять(также в application.properties прописать username и password
        message.setFrom("bbuba0603@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        javaMailSender.send(message);
    }
}
