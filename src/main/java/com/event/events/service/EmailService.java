package com.event.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendOtp(String email, String subject, String message) {

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setSubject(subject);
        mail.setText(message);

        mailSender.send(mail);
    }
}
