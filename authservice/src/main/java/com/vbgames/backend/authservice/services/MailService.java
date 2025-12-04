package com.vbgames.backend.authservice.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final JwtService jwtService;

    @Value("${mail.from}")
    private String sender;

    public void sendVerificationMail(String user, String email) {

        String token = jwtService.createVerifyEmailToken(user, email);
        String subject = "Verify your email";
        String verificationLink = "http://localhost:8080/auth/verify/" + token;
        String content =
            "<div style='font-family: Arial, sans-serif; max-width: 600px; text-align: center;'>"
            + "<h2 style='text-align: center;'>Verifica tu correo</h2>"
            + "<p>Haz clic aquí para verificar tu correo:</p>"
            + "<p style='margin: 25px 0;'>"
            + "<a href='" + verificationLink + "' style='"
            + "background-color: #007bff; color: white; padding: 12px 24px; "
            + "text-decoration: none; border-radius: 5px;'>Verificar Email</a>"
            + "</p>"
            + "<p>Si no solicitaste esto, ignora este correo.</p>"
            + "<hr style='margin: 5px 0; border: 0; border-top: 1px solid #eee;'>"
            + "<p style='font-size: 12px; color: #666;'>"
            + "<strong>No responder a este email.</strong> Mensaje automático.</p>"
            + "</div>";

        MimeMessage message = mailSender.createMimeMessage();
        
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setSubject(subject);
            helper.setTo(email);
            helper.setFrom(sender);
            helper.setText(content, true);
            
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
      
    }
}
