package com.example.alumni.controller;

import com.example.alumni.model.User;
import com.example.alumni.service.UserService;
import com.example.alumni.repository.UserRepository;
import com.example.alumni.service.UserPointsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserPointsService userPointsService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerUser_success() throws Exception {
        System.out.println("\n=== Starting registerUser_success test ===");
        
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setName("John");
        user.setRole("ALUMNI");
        user.setPhone("1234567890");
        user.setBatch("2020");
        user.setRollNo("1234567890");
        user.setDepartment("CSE");
        user.setProfileType("Public");

        when(userService.registerUser(any(User.class))).thenReturn("User registered successfully!");

        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully!"));

        verify(userService, times(1)).registerUser(any(User.class));
        System.out.println("=== End of registerUser_success test ===\n");
    }

    @Test
    void loginUser_success() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userService.loginUser(eq("test@example.com"), eq("password")))
                .thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    void loginUser_failure() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("wrong");

        when(userService.loginUser(eq("test@example.com"), eq("wrong")))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllUsers_shouldReturnList() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setProfileType("Public");
        user.setName("gsav");
        user.setLastName("lname");
        user.setEmail("email");
        user.setPassword("password");
        user.setRole("Alumni");
        user.setPhone("phone");
        user.setBatch("batch");
        user.setRollNo("rollNo");
        user.setDepartment("department");
        user.setImageUrl("imageUrl");

        when(userService.getAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("gsav"));
    }

    @Test
    void getUserById_found() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("User One");

        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("User One"));
    }

    @Test
    void getUserById_notFound() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void logout_success() throws Exception {
        mockMvc.perform(post("/api/logout"))
                .andExpect(status().isOk())
                .andExpect(content().string("Logged out successfully"));
    }
}
