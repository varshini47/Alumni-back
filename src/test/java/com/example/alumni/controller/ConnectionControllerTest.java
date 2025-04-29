package com.example.alumni.controller;

import com.example.alumni.model.Connection;
import com.example.alumni.model.Connection.ConnectionStatus;
import com.example.alumni.model.User;
import com.example.alumni.service.ConnectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ConnectionController.class)
@AutoConfigureMockMvc(addFilters = false)
class ConnectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConnectionService connectionService;

    @Autowired
    private ObjectMapper objectMapper;

    private Connection connection;
    private User sender;
    private User receiver;

    @BeforeEach
    void setUp() {
        System.out.println("\n=== Setting up test ===");
        
        sender = new User();
        sender.setId(1L);
        sender.setName("John");
        sender.setLastName("Doe");
        sender.setEmail("john@example.com");
        
        receiver = new User();
        receiver.setId(2L);
        receiver.setName("Jane");
        receiver.setLastName("Smith");
        receiver.setEmail("jane@example.com");
        
        connection = new Connection();
        connection.setId(1L);
        connection.setSender(sender);
        connection.setReceiver(receiver);
        connection.setStatus(ConnectionStatus.PENDING);
        
        System.out.println("=== Test setup complete ===\n");
    }

    @Test
    void sendRequest_success() throws Exception {
        System.out.println("\n=== Starting sendRequest_success test ===");
        when(connectionService.sendRequest(1L, 2L)).thenReturn(connection);

        mockMvc.perform(post("/api/connections/send/1/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(connectionService).sendRequest(1L, 2L);
        System.out.println("=== End of sendRequest_success test ===\n");
    }

    @Test
    void getPendingRequests_success() throws Exception {
        System.out.println("\n=== Starting getPendingRequests_success test ===");
        List<Connection> pendingConnections = Arrays.asList(connection);
        when(connectionService.getPendingRequests(2L)).thenReturn(pendingConnections);

        mockMvc.perform(get("/api/connections/pending/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("PENDING"));

        verify(connectionService).getPendingRequests(2L);
        System.out.println("=== End of getPendingRequests_success test ===\n");
    }

    @Test
    void getPendingRequests_userNotFound() throws Exception {
        System.out.println("\n=== Starting getPendingRequests_userNotFound test ===");
        when(connectionService.getPendingRequests(999L))
            .thenThrow(new RuntimeException("User with ID 999 not found"));

        mockMvc.perform(get("/api/connections/pending/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User with ID 999 not found"));

        verify(connectionService).getPendingRequests(999L);
        System.out.println("=== End of getPendingRequests_userNotFound test ===\n");
    }

    @Test
    void getConnectedUsers_success() throws Exception {
        System.out.println("\n=== Starting getConnectedUsers_success test ===");
        connection.setStatus(ConnectionStatus.ACCEPTED);
        List<Connection> acceptedConnections = Arrays.asList(connection);
        when(connectionService.getConnectedUsers(1L)).thenReturn(acceptedConnections);

        mockMvc.perform(get("/api/connections/accepted/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("ACCEPTED"));

        verify(connectionService).getConnectedUsers(1L);
        System.out.println("=== End of getConnectedUsers_success test ===\n");
    }

    @Test
    void acceptRequest_success() throws Exception {
        System.out.println("\n=== Starting acceptRequest_success test ===");
        doNothing().when(connectionService).acceptRequest(1L);

        mockMvc.perform(post("/api/connections/accept/1"))
                .andExpect(status().isOk());

        verify(connectionService).acceptRequest(1L);
        System.out.println("=== End of acceptRequest_success test ===\n");
    }

    @Test
    void rejectRequest_success() throws Exception {
        System.out.println("\n=== Starting rejectRequest_success test ===");
        doNothing().when(connectionService).rejectRequest(1L);

        mockMvc.perform(delete("/api/connections/reject/1"))
                .andExpect(status().isOk());

        verify(connectionService).rejectRequest(1L);
        System.out.println("=== End of rejectRequest_success test ===\n");
    }

    @Test
    void getConnections_success() throws Exception {
        System.out.println("\n=== Starting getConnections_success test ===");
        List<Connection> pendingConnections = Arrays.asList(connection);
        List<Connection> acceptedConnections = Arrays.asList(connection);
        when(connectionService.getPendingConnections(1L)).thenReturn(pendingConnections);
        when(connectionService.getConnectedUsers(1L)).thenReturn(acceptedConnections);

        mockMvc.perform(get("/api/connections/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pending[0].id").value(1))
                .andExpect(jsonPath("$.accepted[0].id").value(1));

        verify(connectionService).getPendingConnections(1L);
        verify(connectionService).getConnectedUsers(1L);
        System.out.println("=== End of getConnections_success test ===\n");
    }
} 