package com.example.alumni.controller;

import com.example.alumni.model.ChatGroup;
import com.example.alumni.model.User;
import com.example.alumni.service.GroupService;
import com.example.alumni.repository.GroupMessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GroupController.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser
class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupService groupService;

    @MockBean
    private GroupMessageRepository groupMessageRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ChatGroup testGroup;
    private User testUser;

    @BeforeEach
    void setUp() {
        System.out.println("\n=== Setting up test ===");
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");

        testGroup = new ChatGroup("Test Group", testUser);
        // Use reflection to set the ID since it's not exposed through a setter
        try {
            java.lang.reflect.Field idField = ChatGroup.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(testGroup, 1L);
        } catch (Exception e) {
            System.out.println("Error setting ID: " + e.getMessage());
        }
        System.out.println("=== Test setup complete ===\n");
    }

    @Test
    void createGroup_success() throws Exception {
        System.out.println("\n=== Starting createGroup_success test ===");
        GroupRequest request = new GroupRequest();
        request.setName("Test Group");
        request.setCreatedBy(1L);

        when(groupService.createGroup(anyString(), anyLong())).thenReturn(testGroup);

        mockMvc.perform(post("/api/chat/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Group"));

        verify(groupService).createGroup("Test Group", 1L);
        System.out.println("=== End of createGroup_success test ===\n");
    }

    @Test
    void joinGroup_success() throws Exception {
        System.out.println("\n=== Starting joinGroup_success test ===");
        JoinGroupRequest request = new JoinGroupRequest();
        request.setUserId(1L);

        when(groupService.joinGroup(anyLong(), anyLong())).thenReturn(testGroup);

        mockMvc.perform(post("/api/chat/groups/1/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Group"));

        verify(groupService).joinGroup(1L, 1L);
        System.out.println("=== End of joinGroup_success test ===\n");
    }

    @Test
    void getAllGroups_success() throws Exception {
        System.out.println("\n=== Starting getAllGroups_success test ===");
        List<ChatGroup> groups = Arrays.asList(testGroup);
        when(groupService.getAllGroupsSorted()).thenReturn(groups);

        mockMvc.perform(get("/api/chat/groups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Group"));

        verify(groupService).getAllGroupsSorted();
        System.out.println("=== End of getAllGroups_success test ===\n");
    }

    @Test
    void exitGroup_success() throws Exception {
        System.out.println("\n=== Starting exitGroup_success test ===");
        GroupExitRequest request = new GroupExitRequest();
        request.setGroupId(1L);
        request.setUserId(1L);

        when(groupService.exitGroup(anyLong(), anyLong())).thenReturn(true);

        mockMvc.perform(post("/api/chat/groups/exit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("User exited group successfully"));

        verify(groupService).exitGroup(1L, 1L);
        System.out.println("=== End of exitGroup_success test ===\n");
    }

    @Test
    void exitGroup_failure() throws Exception {
        System.out.println("\n=== Starting exitGroup_failure test ===");
        GroupExitRequest request = new GroupExitRequest();
        request.setGroupId(1L);
        request.setUserId(1L);

        when(groupService.exitGroup(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(post("/api/chat/groups/exit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Failed to exit group"));

        verify(groupService).exitGroup(1L, 1L);
        System.out.println("=== End of exitGroup_failure test ===\n");
    }

    @Test
    void getRecentGroups_success() throws Exception {
        System.out.println("\n=== Starting getRecentGroups_success test ===");
        List<ChatGroup> groups = Arrays.asList(testGroup);
        when(groupMessageRepository.findRecentGroups(anyLong())).thenReturn(groups);

        mockMvc.perform(get("/api/chat/groups/recent/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Group"));

        verify(groupMessageRepository, times(2)).findRecentGroups(1L);
        System.out.println("=== End of getRecentGroups_success test ===\n");
    }
} 