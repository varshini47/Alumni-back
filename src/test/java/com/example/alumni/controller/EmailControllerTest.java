package com.example.alumni.controller;

import com.example.alumni.model.User;
import com.example.alumni.repository.UserRepository;
import com.example.alumni.service.EmailService;
import com.example.alumni.service.UserPointsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import jakarta.mail.MessagingException;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EmailController.class)
@AutoConfigureMockMvc(addFilters = false)
class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailService emailService;

    @MockBean
    private UserPointsService userPointsService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void forgotPassword_userFound() throws Exception {
        System.out.println("\n=== Starting forgotPassword_userFound test ===");
        
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        doNothing().when(emailService).sendResetEmail(anyString(), anyString());

        Map<String, String> request = new HashMap<>();
        request.put("email", "test@example.com");

        mockMvc.perform(post("/api/email/forgot-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset link sent successfully!"));

        verify(emailService).sendResetEmail(anyString(), anyString());
        System.out.println("=== End of forgotPassword_userFound test ===\n");
    }

    @Test
    void forgotPassword_userNotFound() throws Exception {
        System.out.println("\n=== Starting forgotPassword_userNotFound test ===");
        
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        Map<String, String> request = new HashMap<>();
        request.put("email", "test@example.com");

        mockMvc.perform(post("/api/email/forgot-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found!"));

        verify(emailService, never()).sendResetEmail(anyString(), anyString());
        System.out.println("=== End of forgotPassword_userNotFound test ===\n");
    }

    @Test
    void resetPassword_success() throws Exception {
        System.out.println("\n=== Starting resetPassword_success test ===");
        
        User user = new User();
        user.setId(1L);
        user.setPassword("oldPassword");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("resetSession:1", "validSessionId");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");

        Map<String, String> request = new HashMap<>();
        request.put("userId", "1");
        request.put("newPassword", "newPassword");

        mockMvc.perform(post("/api/email/reset-password")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Password updated successfully!"));

        verify(userRepository).save(any(User.class));
        System.out.println("=== End of resetPassword_success test ===\n");
    }

    @Test
    void resetPassword_invalidSession() throws Exception {
        System.out.println("\n=== Starting resetPassword_invalidSession test ===");
        
        MockHttpSession session = new MockHttpSession();
        // Don't set any session attribute to simulate invalid session

        Map<String, String> request = new HashMap<>();
        request.put("userId", "1");
        request.put("newPassword", "newPassword");

        mockMvc.perform(post("/api/email/reset-password")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Invalid or expired reset link!"));

        verify(userRepository, never()).save(any(User.class));
        System.out.println("=== End of resetPassword_invalidSession test ===\n");
    }

    @Test
    void sendInvite_success() throws Exception {
        System.out.println("\n=== Starting sendInvite_success test ===");
        
        Map<String, Object> request = new HashMap<>();
        request.put("userId", 1L);
        request.put("fromEmail", "sender@example.com");
        request.put("toEmails", Arrays.asList("recipient1@example.com", "recipient2@example.com"));
        request.put("name", "John Doe");
        request.put("registrationLink", "http://localhost:3000/register");

        doNothing().when(emailService).sendInvite(anyString(), anyList(), anyString(), anyString());
        when(userPointsService.getPoints(anyLong())).thenReturn(100);

        mockMvc.perform(post("/api/email/invite")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Invite sent successfully!"));

        verify(emailService).sendInvite(anyString(), anyList(), anyString(), anyString());
        verify(userPointsService).addPoints(anyLong(), anyInt());
        System.out.println("=== End of sendInvite_success test ===\n");
    }

    @Test
    void sendInvite_failure() throws Exception {
        System.out.println("\n=== Starting sendInvite_failure test ===");
        
        Map<String, Object> request = new HashMap<>();
        request.put("userId", 1L);
        request.put("fromEmail", "sender@example.com");
        request.put("toEmails", Arrays.asList("recipient1@example.com", "recipient2@example.com"));
        request.put("name", "John Doe");
        request.put("registrationLink", "http://localhost:3000/register");

        doThrow(new MessagingException("Failed to send email")).when(emailService)
                .sendInvite(anyString(), anyList(), anyString(), anyString());

        mockMvc.perform(post("/api/email/invite")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Failed to send invite: Failed to send email"));

        verify(userPointsService, never()).addPoints(anyLong(), anyInt());
        System.out.println("=== End of sendInvite_failure test ===\n");
    }
} 