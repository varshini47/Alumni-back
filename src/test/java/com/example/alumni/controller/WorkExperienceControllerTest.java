package com.example.alumni.controller;

import com.example.alumni.model.WorkExperience;
import com.example.alumni.model.User;
import com.example.alumni.service.WorkExperienceService;
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

@WebMvcTest(WorkExperienceController.class)
@AutoConfigureMockMvc(addFilters = false)
class WorkExperienceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkExperienceService workExperienceService;

    @MockBean
    private UserPointsService userPointsService;

    @Autowired
    private ObjectMapper objectMapper;

    private WorkExperience workExperience;
    private User user;

    @BeforeEach
    void setUp() {
        System.out.println("\n=== Setting up test ===");
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
        System.out.println("=== Test setup complete ===\n");
    }

    @Test
    void addWorkExperience_success() throws Exception {
        System.out.println("\n=== Starting addWorkExperience_success test ===");
        
        when(workExperienceService.addWorkExperience(any(WorkExperience.class))).thenReturn(workExperience);
        doNothing().when(userPointsService).addPoints(anyLong(), anyInt());

        mockMvc.perform(post("/api/work-experience")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(workExperience)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.company").value("Test Company"));

        verify(workExperienceService).addWorkExperience(any(WorkExperience.class));
        verify(userPointsService).addPoints(anyLong(), anyInt());
        System.out.println("=== End of addWorkExperience_success test ===\n");
    }

    @Test
    void getWorkExperiencesByUserId_success() throws Exception {
        System.out.println("\n=== Starting getWorkExperiencesByUserId_success test ===");
        
        List<WorkExperience> experiences = Arrays.asList(workExperience);
        when(workExperienceService.getWorkExperiencesByUserId(1L)).thenReturn(experiences);

        mockMvc.perform(get("/api/work-experience/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].company").value("Test Company"));

        verify(workExperienceService).getWorkExperiencesByUserId(1L);
        System.out.println("=== End of getWorkExperiencesByUserId_success test ===\n");
    }

    @Test
    void getAllWorkExperiences_success() throws Exception {
        System.out.println("\n=== Starting getAllWorkExperiences_success test ===");
        
        List<WorkExperience> experiences = Arrays.asList(workExperience);
        when(workExperienceService.getAllWorkExperiences()).thenReturn(experiences);

        mockMvc.perform(get("/api/work-experience"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].company").value("Test Company"));

        verify(workExperienceService).getAllWorkExperiences();
        System.out.println("=== End of getAllWorkExperiences_success test ===\n");
    }

    @Test
    void getWorkExperienceById_success() throws Exception {
        System.out.println("\n=== Starting getWorkExperienceById_success test ===");
        
        when(workExperienceService.getWorkExperienceById(1L)).thenReturn(workExperience);

        mockMvc.perform(get("/api/work-experience/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.company").value("Test Company"));

        verify(workExperienceService).getWorkExperienceById(1L);
        System.out.println("=== End of getWorkExperienceById_success test ===\n");
    }

    @Test
    void updateWorkExperience_success() throws Exception {
        System.out.println("\n=== Starting updateWorkExperience_success test ===");
        
        when(workExperienceService.updateWorkExperience(anyLong(), any(WorkExperience.class))).thenReturn(workExperience);

        mockMvc.perform(put("/api/work-experience/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(workExperience)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(workExperienceService).updateWorkExperience(anyLong(), any(WorkExperience.class));
        System.out.println("=== End of updateWorkExperience_success test ===\n");
    }

    @Test
    void deleteWorkExperience_success() throws Exception {
        System.out.println("\n=== Starting deleteWorkExperience_success test ===");
        
        when(workExperienceService.getWorkExperienceById(1L)).thenReturn(workExperience);
        doNothing().when(workExperienceService).deleteWorkExperience(1L);
        doNothing().when(userPointsService).addPoints(anyLong(), anyInt());

        mockMvc.perform(delete("/api/work-experience/1"))
                .andExpect(status().isNoContent());

        verify(workExperienceService).deleteWorkExperience(1L);
        verify(userPointsService).addPoints(anyLong(), anyInt());
        System.out.println("=== End of deleteWorkExperience_success test ===\n");
    }
} 