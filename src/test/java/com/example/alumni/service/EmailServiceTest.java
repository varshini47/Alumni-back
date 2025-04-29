package com.example.alumni.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void sendResetEmail_success() {
        // Arrange
        String toEmail = "test@example.com";
        String resetLink = "http://localhost:3000/reset-password";

        // Act
        emailService.sendResetEmail(toEmail, resetLink);

        // Assert
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendInvite_success() throws MessagingException {
        // Arrange
        String fromEmail = "sender@example.com";
        List<String> toEmails = Arrays.asList("recipient1@example.com", "recipient2@example.com");
        String name = "John Doe";
        String registrationLink = "http://localhost:3000/register";

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act
        emailService.sendInvite(fromEmail, toEmails, name, registrationLink);

        // Assert
        verify(mailSender, times(2)).createMimeMessage();
        verify(mailSender, times(2)).send(any(MimeMessage.class));
    }

    @Test
    void sendInvite_withEmptyEmail() throws MessagingException {
        // Arrange
        String fromEmail = "sender@example.com";
        List<String> toEmails = Arrays.asList("", "recipient@example.com", null);
        String name = "John Doe";
        String registrationLink = "http://localhost:3000/register";

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act
        emailService.sendInvite(fromEmail, toEmails, name, registrationLink);

        // Assert
        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }
} 