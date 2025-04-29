package com.example.alumni.controller;

import com.example.alumni.model.JobOpportunity;
import com.example.alumni.service.JobOpportunityService;
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

@WebMvcTest(JobOpportunityController.class)
@AutoConfigureMockMvc(addFilters = false)
class JobOpportunityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobOpportunityService jobOpportunityService;

    @MockBean
    private UserPointsService userPointsService;

    @Autowired
    private ObjectMapper objectMapper;

    private JobOpportunity jobOpportunity;

    @BeforeEach
    void setUp() {
        System.out.println("\n=== Setting up test ===");
        jobOpportunity = new JobOpportunity();
        jobOpportunity.setId(1L);
        jobOpportunity.setTitle("Software Engineer");
        jobOpportunity.setCompany("Tech Corp");
        jobOpportunity.setLocation("New York");
        jobOpportunity.setJobType("Full-time");
        jobOpportunity.setExperienceLevel("Mid-level");
        jobOpportunity.setSkills("Java, Spring Boot");
        jobOpportunity.setDescription("Looking for a skilled software engineer");
        jobOpportunity.setApplicationDeadline(LocalDate.now().plusDays(30));
        jobOpportunity.setApplicationLink("https://example.com/apply");
        jobOpportunity.setContactInfo("hr@example.com");
        jobOpportunity.setUserId(1L);
        System.out.println("=== Test setup complete ===\n");
    }

    @Test
    void getAllJobOpportunities_success() throws Exception {
        System.out.println("\n=== Starting getAllJobOpportunities_success test ===");
        List<JobOpportunity> jobs = Arrays.asList(jobOpportunity);
        when(jobOpportunityService.getAllJobOpportunitys()).thenReturn(jobs);

        mockMvc.perform(get("/api/jobs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Software Engineer"))
                .andExpect(jsonPath("$[0].company").value("Tech Corp"));

        verify(jobOpportunityService).getAllJobOpportunitys();
        System.out.println("=== End of getAllJobOpportunities_success test ===\n");
    }

    @Test
    void addJobOpportunity_success() throws Exception {
        System.out.println("\n=== Starting addJobOpportunity_success test ===");
        when(jobOpportunityService.addJobOpportunity(any(JobOpportunity.class))).thenReturn(jobOpportunity);
        doNothing().when(userPointsService).addPoints(anyLong(), anyInt());

        mockMvc.perform(post("/api/jobs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(jobOpportunity)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Software Engineer"));

        verify(jobOpportunityService).addJobOpportunity(any(JobOpportunity.class));
        verify(userPointsService).addPoints(1L, 10);
        System.out.println("=== End of addJobOpportunity_success test ===\n");
    }

    @Test
    void deleteJob_success_beforeDeadline() throws Exception {
        System.out.println("\n=== Starting deleteJob_success_beforeDeadline test ===");
        when(jobOpportunityService.getJobById(1L)).thenReturn(jobOpportunity);
        doNothing().when(jobOpportunityService).deleteJobOpportunity(1L);
        doNothing().when(userPointsService).addPoints(anyLong(), anyInt());

        mockMvc.perform(delete("/api/jobs/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Job deleted successfully"));

        verify(jobOpportunityService).getJobById(1L);
        verify(jobOpportunityService).deleteJobOpportunity(1L);
        verify(userPointsService, never()).addPoints(anyLong(), eq(-10));
        System.out.println("=== End of deleteJob_success_beforeDeadline test ===\n");
    }

    @Test
    void deleteJob_success_afterDeadline() throws Exception {
        System.out.println("\n=== Starting deleteJob_success_afterDeadline test ===");
        jobOpportunity.setApplicationDeadline(LocalDate.now().minusDays(1));
        when(jobOpportunityService.getJobById(1L)).thenReturn(jobOpportunity);
        doNothing().when(jobOpportunityService).deleteJobOpportunity(1L);
        doNothing().when(userPointsService).addPoints(anyLong(), anyInt());

        mockMvc.perform(delete("/api/jobs/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Job deleted successfully"));

        verify(jobOpportunityService).getJobById(1L);
        verify(jobOpportunityService).deleteJobOpportunity(1L);
        verify(userPointsService).addPoints(1L, -10);
        System.out.println("=== End of deleteJob_success_afterDeadline test ===\n");
    }

    @Test
    void deleteJob_notFound() throws Exception {
        System.out.println("\n=== Starting deleteJob_notFound test ===");
        when(jobOpportunityService.getJobById(1L)).thenReturn(null);

        mockMvc.perform(delete("/api/jobs/1"))
                .andExpect(status().isNotFound());

        verify(jobOpportunityService).getJobById(1L);
        verify(jobOpportunityService, never()).deleteJobOpportunity(anyLong());
        verify(userPointsService, never()).addPoints(anyLong(), anyInt());
        System.out.println("=== End of deleteJob_notFound test ===\n");
    }
} 