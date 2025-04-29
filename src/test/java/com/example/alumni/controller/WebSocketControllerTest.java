package com.example.alumni.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Collections;
import java.util.Set;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Assertions;

class WebSocketControllerTest {

    @Mock
    private SimpUserRegistry simpUserRegistry;

    @InjectMocks
    private WebSocketController webSocketController;

    @BeforeEach
    void setUp() {
        System.out.println("\n=== Setting up test ===");
        MockitoAnnotations.openMocks(this);
        System.out.println("=== Test setup complete ===\n");
    }

    @Test
    void checkConnectedUsers_noUsers() throws Exception {
        System.out.println("\n=== Starting checkConnectedUsers_noUsers test ===");
        when(simpUserRegistry.getUsers()).thenReturn(Collections.emptySet());

        webSocketController.checkConnectedUsers();

        verify(simpUserRegistry).getUsers();
        System.out.println("=== End of checkConnectedUsers_noUsers test ===\n");
    }

    @Test
    void checkConnectedUsers_withUsers() throws Exception {
        System.out.println("\n=== Starting checkConnectedUsers_withUsers test ===");
        SimpUser mockUser = mock(SimpUser.class);
        Set<SimpUser> users = Collections.singleton(mockUser);
        when(simpUserRegistry.getUsers()).thenReturn(users);

        webSocketController.checkConnectedUsers();

        verify(simpUserRegistry).getUsers();
        System.out.println("=== End of checkConnectedUsers_withUsers test ===\n");
    }

    @Test
    void checkConnectedUsers_multipleUsers() throws Exception {
        System.out.println("\n=== Starting checkConnectedUsers_multipleUsers test ===");
        SimpUser mockUser1 = mock(SimpUser.class);
        SimpUser mockUser2 = mock(SimpUser.class);
        Set<SimpUser> users = Set.of(mockUser1, mockUser2);
        when(simpUserRegistry.getUsers()).thenReturn(users);

        webSocketController.checkConnectedUsers();

        verify(simpUserRegistry).getUsers();
        System.out.println("=== End of checkConnectedUsers_multipleUsers test ===\n");
    }

    @Test
    void checkConnectedUsers_exceptionHandling() {
        System.out.println("\n=== Starting checkConnectedUsers_exceptionHandling test ===");
        when(simpUserRegistry.getUsers()).thenThrow(new RuntimeException("Test exception"));

        // Verify that the method doesn't throw an exception
        Assertions.assertDoesNotThrow(() -> webSocketController.checkConnectedUsers());

        verify(simpUserRegistry).getUsers();
        System.out.println("=== End of checkConnectedUsers_exceptionHandling test ===\n");
    }
} 