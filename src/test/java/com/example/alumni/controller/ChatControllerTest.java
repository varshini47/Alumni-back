package com.example.alumni.controller;

import com.example.alumni.model.Message;
import com.example.alumni.model.User;
import com.example.alumni.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ChatController.class)
@AutoConfigureMockMvc(addFilters = false)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageRepository messageRepository;

    @MockBean
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private Message message;
    private User user;

    @BeforeEach
    void setUp() {
        System.out.println("\n=== Setting up test ===");
        message = new Message();
        message.setId(1L);
        message.setSenderId(1L);
        message.setReceiverId(2L);
        message.setContent("Hello!");
        message.setTimestamp(LocalDateTime.now());

        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        System.out.println("=== Test setup complete ===\n");
    }

    @Test
    void getRecentContacts_success() throws Exception {
        System.out.println("\n=== Starting getRecentContacts_success test ===");
        List<User> contacts = Arrays.asList(user);
        when(messageRepository.findRecentContacts(1L)).thenReturn(contacts);

        mockMvc.perform(get("/api/chat/recent/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test User"));

        verify(messageRepository).findRecentContacts(1L);
        System.out.println("=== End of getRecentContacts_success test ===\n");
    }

    @Test
    void getChatHistory_success() throws Exception {
        System.out.println("\n=== Starting getChatHistory_success test ===");
        List<Message> messages = Arrays.asList(message);
        when(messageRepository.findBySenderIdOrReceiverId(1L, 1L)).thenReturn(messages);

        mockMvc.perform(get("/api/chat/history/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].content").value("Hello!"));

        verify(messageRepository).findBySenderIdOrReceiverId(1L, 1L);
        System.out.println("=== End of getChatHistory_success test ===\n");
    }

    @Test
    void getChatHistoryBetweenUsers_success() throws Exception {
        System.out.println("\n=== Starting getChatHistoryBetweenUsers_success test ===");
        List<Message> messages = Arrays.asList(message);
        when(messageRepository.findBySenderAndReceiver(1L, 2L)).thenReturn(messages);

        mockMvc.perform(get("/api/chat/history/1/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].content").value("Hello!"));

        verify(messageRepository).findBySenderAndReceiver(1L, 2L);
        System.out.println("=== End of getChatHistoryBetweenUsers_success test ===\n");
    }

    @Test
    void editMessage_success() throws Exception {
        System.out.println("\n=== Starting editMessage_success test ===");
        when(messageRepository.findById(1L)).thenReturn(java.util.Optional.of(message));
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        mockMvc.perform(put("/api/chat/edit/1")
                .contentType("text/plain")
                .content("Updated message"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("Updated message"));

        verify(messageRepository).findById(1L);
        verify(messageRepository).save(any(Message.class));
        verify(messagingTemplate).convertAndSendToUser(anyString(), anyString(), any(Message.class));
        System.out.println("=== End of editMessage_success test ===\n");
    }

    @Test
    void editMessage_notFound() throws Exception {
        System.out.println("\n=== Starting editMessage_notFound test ===");
        when(messageRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(put("/api/chat/edit/1")
                .contentType("text/plain")
                .content("Updated message"))
                .andExpect(status().isNotFound());

        verify(messageRepository).findById(1L);
        verify(messageRepository, never()).save(any(Message.class));
        verify(messagingTemplate, never()).convertAndSendToUser(anyString(), anyString(), any(Message.class));
        System.out.println("=== End of editMessage_notFound test ===\n");
    }

    @Test
    void deleteMessage_success() throws Exception {
        System.out.println("\n=== Starting deleteMessage_success test ===");
        when(messageRepository.existsById(1L)).thenReturn(true);
        doNothing().when(messageRepository).deleteById(1L);

        mockMvc.perform(delete("/api/chat/delete/1"))
                .andExpect(status().isNoContent());

        verify(messageRepository).existsById(1L);
        verify(messageRepository).deleteById(1L);
        System.out.println("=== End of deleteMessage_success test ===\n");
    }

    @Test
    void deleteMessage_notFound() throws Exception {
        System.out.println("\n=== Starting deleteMessage_notFound test ===");
        when(messageRepository.existsById(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/chat/delete/1"))
                .andExpect(status().isNotFound());

        verify(messageRepository).existsById(1L);
        verify(messageRepository, never()).deleteById(anyLong());
        System.out.println("=== End of deleteMessage_notFound test ===\n");
    }
} 