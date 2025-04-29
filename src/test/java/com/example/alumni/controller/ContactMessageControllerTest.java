package com.example.alumni.controller;

import com.example.alumni.model.ContactMessage;
import com.example.alumni.repository.ContactMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ContactMessageController.class)
@AutoConfigureMockMvc(addFilters = false)
class ContactMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactMessageRepository contactMessageRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ContactMessage contactMessage;

    @BeforeEach
    void setUp() {
        System.out.println("\n=== Setting up test ===");
        contactMessage = new ContactMessage();
        contactMessage.setId(1L);
        contactMessage.setName("John Doe");
        contactMessage.setEmail("john@example.com");
        contactMessage.setMessage("Test message");
        contactMessage.setCreatedAt(LocalDateTime.now());
        contactMessage.setResolved(false);
        System.out.println("=== Test setup complete ===\n");
    }

    @Test
    void submitContactForm_success() throws Exception {
        System.out.println("\n=== Starting submitContactForm_success test ===");
        when(contactMessageRepository.save(any(ContactMessage.class))).thenReturn(contactMessage);

        mockMvc.perform(post("/api/contact/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contactMessage)))
                .andExpect(status().isOk())
                .andExpect(content().string("Message received successfully!"));

        verify(contactMessageRepository).save(any(ContactMessage.class));
        System.out.println("=== End of submitContactForm_success test ===\n");
    }

    @Test
    void getAllContactMessages_success() throws Exception {
        System.out.println("\n=== Starting getAllContactMessages_success test ===");
        List<ContactMessage> messages = Arrays.asList(contactMessage);
        when(contactMessageRepository.findAll()).thenReturn(messages);

        mockMvc.perform(get("/api/contact/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("john@example.com"));

        verify(contactMessageRepository).findAll();
        System.out.println("=== End of getAllContactMessages_success test ===\n");
    }

    @Test
    void markAsResolved_success() throws Exception {
        System.out.println("\n=== Starting markAsResolved_success test ===");
        when(contactMessageRepository.findById(1L)).thenReturn(Optional.of(contactMessage));
        when(contactMessageRepository.save(any(ContactMessage.class))).thenReturn(contactMessage);

        mockMvc.perform(put("/api/contact/1/resolve"))
                .andExpect(status().isOk())
                .andExpect(content().string("Message marked as resolved"));

        verify(contactMessageRepository).findById(1L);
        verify(contactMessageRepository).save(any(ContactMessage.class));
        System.out.println("=== End of markAsResolved_success test ===\n");
    }

    @Test
    void markAsResolved_notFound() throws Exception {
        System.out.println("\n=== Starting markAsResolved_notFound test ===");
        when(contactMessageRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/contact/1/resolve"))
                .andExpect(status().isNotFound());

        verify(contactMessageRepository).findById(1L);
        verify(contactMessageRepository, never()).save(any(ContactMessage.class));
        System.out.println("=== End of markAsResolved_notFound test ===\n");
    }

    @Test
    void deleteMessage_success() throws Exception {
        System.out.println("\n=== Starting deleteMessage_success test ===");
        when(contactMessageRepository.existsById(1L)).thenReturn(true);
        doNothing().when(contactMessageRepository).deleteById(1L);

        mockMvc.perform(delete("/api/contact/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Message deleted successfully"));

        verify(contactMessageRepository).existsById(1L);
        verify(contactMessageRepository).deleteById(1L);
        System.out.println("=== End of deleteMessage_success test ===\n");
    }

    @Test
    void deleteMessage_notFound() throws Exception {
        System.out.println("\n=== Starting deleteMessage_notFound test ===");
        when(contactMessageRepository.existsById(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/contact/1"))
                .andExpect(status().isNotFound());

        verify(contactMessageRepository).existsById(1L);
        verify(contactMessageRepository, never()).deleteById(anyLong());
        System.out.println("=== End of deleteMessage_notFound test ===\n");
    }
} 