package com.example.server1.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String recipientEmail, String token) {
        String subject = "Email Verification";
        String verificationUrl = "http://localhost:8081/security/verify?token=" + token;
        String messageBody = "Please click the following link to verify your email: " + verificationUrl;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject(subject);
        message.setText(messageBody);
        message.setFrom("prasath0807@gmail.com"); // Optional: set the sender email if different from default

        mailSender.send(message);
    }
}

