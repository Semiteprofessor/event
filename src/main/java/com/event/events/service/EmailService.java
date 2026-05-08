package com.event.events.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public void sendOtpEmail(String to, String name, String otp) {
        try {
            Context context = new Context();
            context.setVariable("subject", "Verify your email");
            context.setVariable("bannerTitle", "Welcome to BeeCron");
            context.setVariable("firstName", name);
            context.setVariable("otp", otp);

            String html = templateEngine.process("welcome-email", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Verify your email");
            helper.setText(html, true);

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendInstallmentPaymentMail(
            String userName,
            String email,
            String subject,
            String eventName,
            String ticketName,
            double amountPaid,
            double totalPaid,
            double remainingAmount,
            int installmentsPaid,
            int numberOfInstallments,
            String templateFile,
            String time
    ) {
        try {
            Context context = new Context();
            context.setVariable("userName", userName);
            context.setVariable("eventName", eventName);
            context.setVariable("ticketName", ticketName);
            context.setVariable("amountPaid", amountPaid);
            context.setVariable("totalPaid", totalPaid);
            context.setVariable("remainingAmount", remainingAmount);
            context.setVariable("installmentsPaid", installmentsPaid);
            context.setVariable("numberOfInstallments", numberOfInstallments);
            context.setVariable("time", time);

            String html = templateEngine.process(templateFile, context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(html, true);

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send installment email", e);
        }
    }
}