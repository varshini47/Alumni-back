package com.example.alumni.controller;

import com.example.alumni.model.User;
import com.example.alumni.model.WorkExperience;
import com.example.alumni.service.SearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(SearchController.class)
@AutoConfigureMockMvc(addFilters = false)
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchService searchService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private WorkExperience workExperience;

    @BeforeEach
    void setUp() {
        System.out.println("\n=== Setting up test ===");
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setBatch("2020");

        workExperience = new WorkExperience();
        workExperience.setId(1L);
        workExperience.setCompany("Test Company");
        workExperience.setUser(user);
        System.out.println("=== Test setup complete ===\n");
    }

    @Test
    void searchByName_success() throws Exception {
        System.out.println("\n=== Starting searchByName_success test ===");
        List<User> users = Arrays.asList(user);
        when(searchService.search("name", "Test")).thenReturn(users);

        mockMvc.perform(get("/api/search")
                .param("type", "name")
                .param("query", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test User"));

        verify(searchService).search("name", "Test");
        System.out.println("=== End of searchByName_success test ===\n");
    }

    @Test
    void searchByCompany_success() throws Exception {
        System.out.println("\n=== Starting searchByCompany_success test ===");
        List<User> users = Arrays.asList(user);
        when(searchService.search("company_name", "Test")).thenReturn(users);

        mockMvc.perform(get("/api/search")
                .param("type", "company_name")
                .param("query", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test User"));

        verify(searchService).search("company_name", "Test");
        System.out.println("=== End of searchByCompany_success test ===\n");
    }

    @Test
    void searchByBatch_success() throws Exception {
        System.out.println("\n=== Starting searchByBatch_success test ===");
        List<User> users = Arrays.asList(user);
        when(searchService.search("batch", "2020")).thenReturn(users);

        mockMvc.perform(get("/api/search")
                .param("type", "batch")
                .param("query", "2020"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].batch").value("2020"));

        verify(searchService).search("batch", "2020");
        System.out.println("=== End of searchByBatch_success test ===\n");
    }

    @Test
    void search_emptyResults() throws Exception {
        System.out.println("\n=== Starting search_emptyResults test ===");
        when(searchService.search(anyString(), anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/search")
                .param("type", "invalid_type")
                .param("query", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(searchService).search("invalid_type", "test");
        System.out.println("=== End of search_emptyResults test ===\n");
    }

    @Test
    void search_missingParameters() throws Exception {
        System.out.println("\n=== Starting search_missingParameters test ===");
        mockMvc.perform(get("/api/search"))
                .andExpect(status().isBadRequest());

        verify(searchService, never()).search(anyString(), anyString());
        System.out.println("=== End of search_missingParameters test ===\n");
    }
} 