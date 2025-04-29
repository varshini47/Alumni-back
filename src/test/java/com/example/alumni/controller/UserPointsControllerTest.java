package com.example.alumni.controller;

import com.example.alumni.dto.UserLeaderboardDTO;
import com.example.alumni.model.User;
import com.example.alumni.service.UserPointsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserPointsController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserPointsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserPointsService userPointsService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private UserLeaderboardDTO leaderboardDTO;

    @BeforeEach
    void setUp() {
        System.out.println("\n=== Setting up test ===");
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setLastName("Doe");
        
        leaderboardDTO = new UserLeaderboardDTO(user, 100);
        System.out.println("=== Test setup complete ===\n");
    }

    @Test
    void getUserPoints_success() throws Exception {
        System.out.println("\n=== Starting getUserPoints_success test ===");
        when(userPointsService.getPoints(1L)).thenReturn(100);

        mockMvc.perform(get("/api/users/1/points"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.points").value(100));

        verify(userPointsService).getPoints(1L);
        System.out.println("=== End of getUserPoints_success test ===\n");
    }

    @Test
    void getUserPoints_zeroPoints() throws Exception {
        System.out.println("\n=== Starting getUserPoints_zeroPoints test ===");
        when(userPointsService.getPoints(1L)).thenReturn(0);

        mockMvc.perform(get("/api/users/1/points"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.points").value(0));

        verify(userPointsService).getPoints(1L);
        System.out.println("=== End of getUserPoints_zeroPoints test ===\n");
    }

    @Test
    void getLeaderboard_success() throws Exception {
        System.out.println("\n=== Starting getLeaderboard_success test ===");
        List<UserLeaderboardDTO> leaderboard = Arrays.asList(leaderboardDTO);
        when(userPointsService.getLeaderboard()).thenReturn(leaderboard);

        mockMvc.perform(get("/api/users/leaderboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test User Doe"))
                .andExpect(jsonPath("$[0].points").value(100))
                .andExpect(jsonPath("$[0].email").value("test@example.com"));

        verify(userPointsService).getLeaderboard();
        System.out.println("=== End of getLeaderboard_success test ===\n");
    }

    @Test
    void getLeaderboard_empty() throws Exception {
        System.out.println("\n=== Starting getLeaderboard_empty test ===");
        List<UserLeaderboardDTO> emptyList = Arrays.asList();
        when(userPointsService.getLeaderboard()).thenReturn(emptyList);

        mockMvc.perform(get("/api/users/leaderboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(userPointsService).getLeaderboard();
        System.out.println("=== End of getLeaderboard_empty test ===\n");
    }
} 