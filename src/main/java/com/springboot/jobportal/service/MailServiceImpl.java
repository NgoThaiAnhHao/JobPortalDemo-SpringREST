package com.springboot.jobportal.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
@AllArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void sendVerificationToken(String receiverEmail, String token) throws MessagingException {
        // Create context to send "token" variable
        Context context = new Context();
        context.setVariable("token", token);

        // Reading html file
        String htmlContent = templateEngine.process("otp-message", context);

        // Generate message
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("ngothaianhhao@gmail.com");
        helper.setTo(receiverEmail);
        helper.setSubject("Verify Your Email");
        helper.setText(htmlContent);

        javaMailSender.send(message);
    }
}
