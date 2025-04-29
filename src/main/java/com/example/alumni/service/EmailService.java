package com.example.alumni.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    
    public void sendResetEmail(String toEmail, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Password Reset Request");
        message.setText("Click the link to reset your password: " + resetLink);
        mailSender.send(message);
    }
   
    public void sendInvite(String fromEmail, List<String> toEmails,String name, String registrationLink) throws MessagingException {
        System.out.println("entered email service-1");

        for (String toEmail : toEmails) {
            if (toEmail == null || toEmail.trim().isEmpty()) continue; // Skip empty emails
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Join the Alumni Network!");
            //helper.setText("Click the link to register: " + registrationLink, true);
            helper.setText("<p><b>" + name + "</b> invited you to join the Alumni Network!</p>"
              + "<p>Click the link to register: <a href='" + registrationLink + "'>" + registrationLink + "</a></p>",true);
            mailSender.send(message);
            System.out.println("Invite sent to: " + toEmail);
        }
    }
}


