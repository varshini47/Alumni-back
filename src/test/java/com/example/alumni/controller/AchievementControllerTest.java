package com.example.alumni.controller;

import com.example.alumni.dto.AchievementDTO;
import com.example.alumni.model.Achievement;
import com.example.alumni.repository.AchievementRepository;
import com.example.alumni.service.AchievementService;
import com.example.alumni.service.UserPointsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AchievementController.class)
@AutoConfigureMockMvc(addFilters = false)
class AchievementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AchievementService achievementService;

    @MockBean
    private UserPointsService userPointsService;

    @MockBean
    private AchievementRepository achievementRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Achievement achievement;
    private AchievementDTO achievementDTO;

    @BeforeEach
    void setUp() {
        System.out.println("\n=== Setting up test ===");
        achievement = new Achievement();
        achievement.setId(1L);
        achievement.setUserId(1L);
        achievement.setTitle("Test Achievement");
        achievement.setDateOfAchievement(LocalDate.now());
        achievement.setCategory("Academic");
        achievement.setDescription("Test Description");
        achievement.setSupportingDocuments("test.pdf");
        achievement.setOrganization("Test Organization");
        achievement.setAchievedBy("Test User");

        achievementDTO = new AchievementDTO();
        achievementDTO.setId(1L);
        achievementDTO.setTitle("Test Achievement");
        achievementDTO.setCategory("Academic");
        achievementDTO.setDateOfAchievement(LocalDate.now());
        achievementDTO.setDescription("Test Description");
        achievementDTO.setOrganization("Test Organization");
        achievementDTO.setSupportingDocuments("test.pdf");
        achievementDTO.setAlumniName("Test User");
        System.out.println("=== Test setup complete ===\n");
    }

    @Test
    void getAllAchievements_success() throws Exception {
        System.out.println("\n=== Starting getAllAchievements_success test ===");
        List<AchievementDTO> achievements = Arrays.asList(achievementDTO);
        when(achievementRepository.getAllAchievementsWithUser()).thenReturn(achievements);

        mockMvc.perform(get("/api/achievements/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Achievement"));

        verify(achievementRepository).getAllAchievementsWithUser();
        System.out.println("=== End of getAllAchievements_success test ===\n");
    }

    @Test
    void getAchievementById_success() throws Exception {
        System.out.println("\n=== Starting getAchievementById_success test ===");
        when(achievementService.getAchievementById(1L)).thenReturn(achievement);

        mockMvc.perform(get("/api/achievements/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Achievement"));

        verify(achievementService).getAchievementById(1L);
        System.out.println("=== End of getAchievementById_success test ===\n");
    }

    @Test
    void updateAchievement_success() throws Exception {
        System.out.println("\n=== Starting updateAchievement_success test ===");
        when(achievementService.updateAchievement(anyLong(), any(Achievement.class))).thenReturn(achievement);

        mockMvc.perform(put("/api/achievements/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(achievement)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(achievementService).updateAchievement(anyLong(), any(Achievement.class));
        System.out.println("=== End of updateAchievement_success test ===\n");
    }

    @Test
    void addAchievement_success() throws Exception {
        System.out.println("\n=== Starting addAchievement_success test ===");
        when(achievementService.addAchievement(any(Achievement.class))).thenReturn(achievement);
        doNothing().when(userPointsService).addPoints(anyLong(), anyInt());

        mockMvc.perform(post("/api/achievements")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(achievement)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(achievementService).addAchievement(any(Achievement.class));
        verify(userPointsService).addPoints(anyLong(), anyInt());
        System.out.println("=== End of addAchievement_success test ===\n");
    }

    @Test
    void getAchievementsByUserId_success() throws Exception {
        System.out.println("\n=== Starting getAchievementsByUserId_success test ===");
        List<Achievement> achievements = Arrays.asList(achievement);
        when(achievementService.getAchievementsByUserId(1L)).thenReturn(achievements);

        mockMvc.perform(get("/api/achievements/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Achievement"));

        verify(achievementService).getAchievementsByUserId(1L);
        System.out.println("=== End of getAchievementsByUserId_success test ===\n");
    }

    @Test
    void deleteAchievement_success() throws Exception {
        System.out.println("\n=== Starting deleteAchievement_success test ===");
        when(achievementService.getAchievementById(1L)).thenReturn(achievement);
        doNothing().when(achievementService).deleteAchievement(1L);
        doNothing().when(userPointsService).addPoints(anyLong(), anyInt());

        mockMvc.perform(delete("/api/achievements/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Job deleted successfully"));

        verify(achievementService).deleteAchievement(1L);
        verify(userPointsService).addPoints(anyLong(), anyInt());
        System.out.println("=== End of deleteAchievement_success test ===\n");
    }
} 