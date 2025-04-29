package com.example.alumni.controller;

import com.example.alumni.model.Event;
import com.example.alumni.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EventController.class)
@AutoConfigureMockMvc(addFilters = false)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

    private Event event;

    @BeforeEach
    void setUp() {
        System.out.println("\n=== Setting up test ===");
        event = new Event();
        event.setId(1L);
        event.setEventName("Test Event");
        event.setDescription("Test Description");
        event.setEventType("Conference");
        event.setOrganizer("Test Organizer");
        event.setDate("2024-12-31");
        event.setVenue("Test Venue");
        event.setContactPersonEmail("test@example.com");
        event.setSponsorshipDetails("Test Sponsorship");
        System.out.println("=== Test setup complete ===\n");
    }

    @Test
    void getAllEvents_success() throws Exception {
        System.out.println("\n=== Starting getAllEvents_success test ===");
        List<Event> events = Arrays.asList(event);
        when(eventService.getAllEvents()).thenReturn(events);

        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].eventName").value("Test Event"));

        verify(eventService).getAllEvents();
        System.out.println("=== End of getAllEvents_success test ===\n");
    }

    @Test
    void createEvent_success() throws Exception {
        System.out.println("\n=== Starting createEvent_success test ===");
        when(eventService.createEvent(any(Event.class))).thenReturn(event);

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.eventName").value("Test Event"));

        verify(eventService).createEvent(any(Event.class));
        System.out.println("=== End of createEvent_success test ===\n");
    }

    @Test
    void deleteEvent_success() throws Exception {
        System.out.println("\n=== Starting deleteEvent_success test ===");
        doNothing().when(eventService).deleteEvents(1L);

        mockMvc.perform(delete("/api/events/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Job deleted successfully"));

        verify(eventService).deleteEvents(1L);
        System.out.println("=== End of deleteEvent_success test ===\n");
    }
} 