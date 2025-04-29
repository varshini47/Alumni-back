package com.example.alumni.service;

import com.example.alumni.model.User;
import com.example.alumni.model.WorkExperience;
import com.example.alumni.repository.WorkExperienceRepository;
import com.example.alumni.repository.UserRepository;
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
class WorkExperienceServiceTest {

    @Mock
    private WorkExperienceRepository workExperienceRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private WorkExperienceService workExperienceService;

    private User user;
    private WorkExperience workExperience;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");

        workExperience = new WorkExperience();
        workExperience.setId(1L);
        workExperience.setUser(user);
        workExperience.setCompany("Test Company");
        workExperience.setRole("Test Role");
        workExperience.setLocation("Test Location");
        workExperience.setStartDate(LocalDate.now());
        workExperience.setEndDate(LocalDate.now().plusYears(1));
        workExperience.setIsPresent(false);
    }

    @Test
    void addWorkExperience_success() {
        when(workExperienceRepository.save(any(WorkExperience.class))).thenReturn(workExperience);

        WorkExperience result = workExperienceService.addWorkExperience(workExperience);

        assertNotNull(result);
        assertEquals(workExperience.getId(), result.getId());
        verify(workExperienceRepository).save(workExperience);
    }

    @Test
    void getWorkExperiencesByUserId_success() {
        List<WorkExperience> experiences = Arrays.asList(workExperience);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(workExperienceRepository.findByUser(user)).thenReturn(experiences);

        List<WorkExperience> result = workExperienceService.getWorkExperiencesByUserId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(workExperience.getId(), result.get(0).getId());
    }

    @Test
    void getWorkExperiencesByUserId_userNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            workExperienceService.getWorkExperiencesByUserId(1L);
        });
    }

    @Test
    void getAllWorkExperiences_success() {
        List<WorkExperience> experiences = Arrays.asList(workExperience);
        when(workExperienceRepository.findAllWithUser()).thenReturn(experiences);

        List<WorkExperience> result = workExperienceService.getAllWorkExperiences();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(workExperience.getId(), result.get(0).getId());
    }

    @Test
    void deleteWorkExperience_success() {
        doNothing().when(workExperienceRepository).deleteById(1L);

        workExperienceService.deleteWorkExperience(1L);

        verify(workExperienceRepository).deleteById(1L);
    }

    @Test
    void getWorkExperienceById_success() {
        when(workExperienceRepository.findById(1L)).thenReturn(Optional.of(workExperience));

        WorkExperience result = workExperienceService.getWorkExperienceById(1L);

        assertNotNull(result);
        assertEquals(workExperience.getId(), result.getId());
    }

    @Test
    void getWorkExperienceById_notFound() {
        when(workExperienceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            workExperienceService.getWorkExperienceById(1L);
        });
    }

    @Test
    void updateWorkExperience_success() {
        WorkExperience updatedExperience = new WorkExperience();
        updatedExperience.setCompany("Updated Company");
        updatedExperience.setRole("Updated Role");
        updatedExperience.setLocation("Updated Location");
        updatedExperience.setStartDate(LocalDate.now());
        updatedExperience.setEndDate(LocalDate.now().plusYears(2));
        updatedExperience.setIsPresent(false);

        when(workExperienceRepository.findById(1L)).thenReturn(Optional.of(workExperience));
        when(workExperienceRepository.save(any(WorkExperience.class))).thenReturn(updatedExperience);

        WorkExperience result = workExperienceService.updateWorkExperience(1L, updatedExperience);

        assertNotNull(result);
        assertEquals("Updated Company", result.getCompany());
        assertEquals("Updated Role", result.getRole());
        verify(workExperienceRepository).save(any(WorkExperience.class));
    }

    @Test
    void updateWorkExperience_notFound() {
        WorkExperience updatedExperience = new WorkExperience();
        when(workExperienceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            workExperienceService.updateWorkExperience(1L, updatedExperience);
        });
    }
} 