package com.example.alumni.service;

import com.example.alumni.model.Achievement;
import com.example.alumni.repository.AchievementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AchievementServiceTest {

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private UserPointsService userPointsService;

    @InjectMocks
    private AchievementService achievementService;

    private Achievement achievement;

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
        System.out.println("=== Test setup complete ===\n");
    }

    @Test
    void getAllAchievements_success() {
        System.out.println("\n=== Starting getAllAchievements_success test ===");
        List<Achievement> achievements = Arrays.asList(achievement);
        when(achievementRepository.findAll()).thenReturn(achievements);

        List<Achievement> result = achievementService.getAllAchievements();

        assertEquals(1, result.size());
        assertEquals(achievement, result.get(0));
        verify(achievementRepository).findAll();
        System.out.println("=== End of getAllAchievements_success test ===\n");
    }

    @Test
    void addAchievement_success() {
        System.out.println("\n=== Starting addAchievement_success test ===");
        when(achievementRepository.save(any(Achievement.class))).thenReturn(achievement);
        when(userPointsService.getPoints(anyLong())).thenReturn(100);

        Achievement result = achievementService.addAchievement(achievement);

        assertNotNull(result);
        assertEquals(achievement, result);
        verify(achievementRepository).save(any(Achievement.class));
        System.out.println("=== End of addAchievement_success test ===\n");
    }

    @Test
    void getAchievementsByUserId_success() {
        System.out.println("\n=== Starting getAchievementsByUserId_success test ===");
        List<Achievement> achievements = Arrays.asList(achievement);
        when(achievementRepository.findByUserId(1L)).thenReturn(achievements);

        List<Achievement> result = achievementService.getAchievementsByUserId(1L);

        assertEquals(1, result.size());
        assertEquals(achievement, result.get(0));
        verify(achievementRepository).findByUserId(1L);
        System.out.println("=== End of getAchievementsByUserId_success test ===\n");
    }

    @Test
    void getAchievementById_success() {
        System.out.println("\n=== Starting getAchievementById_success test ===");
        when(achievementRepository.findById(1L)).thenReturn(Optional.of(achievement));

        Achievement result = achievementService.getAchievementById(1L);

        assertNotNull(result);
        assertEquals(achievement, result);
        verify(achievementRepository).findById(1L);
        System.out.println("=== End of getAchievementById_success test ===\n");
    }

    @Test
    void getAchievementById_notFound() {
        System.out.println("\n=== Starting getAchievementById_notFound test ===");
        when(achievementRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> achievementService.getAchievementById(1L));
        verify(achievementRepository).findById(1L);
        System.out.println("=== End of getAchievementById_notFound test ===\n");
    }

    @Test
    void updateAchievement_success() {
        System.out.println("\n=== Starting updateAchievement_success test ===");
        Achievement updatedAchievement = new Achievement();
        updatedAchievement.setTitle("Updated Title");
        updatedAchievement.setDescription("Updated Description");

        when(achievementRepository.findById(1L)).thenReturn(Optional.of(achievement));
        when(achievementRepository.save(any(Achievement.class))).thenReturn(achievement);

        Achievement result = achievementService.updateAchievement(1L, updatedAchievement);

        assertNotNull(result);
        verify(achievementRepository).findById(1L);
        verify(achievementRepository).save(any(Achievement.class));
        System.out.println("=== End of updateAchievement_success test ===\n");
    }

    @Test
    void updateAchievement_notFound() {
        System.out.println("\n=== Starting updateAchievement_notFound test ===");
        Achievement updatedAchievement = new Achievement();
        when(achievementRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> achievementService.updateAchievement(1L, updatedAchievement));
        verify(achievementRepository).findById(1L);
        verify(achievementRepository, never()).save(any(Achievement.class));
        System.out.println("=== End of updateAchievement_notFound test ===\n");
    }

    @Test
    void deleteAchievement_success() {
        System.out.println("\n=== Starting deleteAchievement_success test ===");
        doNothing().when(achievementRepository).deleteById(1L);

        achievementService.deleteAchievement(1L);

        verify(achievementRepository).deleteById(1L);
        System.out.println("=== End of deleteAchievement_success test ===\n");
    }
} 