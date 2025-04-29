package com.example.alumni.service;

import com.example.alumni.model.Event;
import com.example.alumni.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

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
    void getAllEvents_success() {
        System.out.println("\n=== Starting getAllEvents_success test ===");
        List<Event> events = Arrays.asList(event);
        when(eventRepository.findAll()).thenReturn(events);

        List<Event> result = eventService.getAllEvents();

        assertEquals(1, result.size());
        assertEquals(event, result.get(0));
        verify(eventRepository).findAll();
        System.out.println("=== End of getAllEvents_success test ===\n");
    }

    @Test
    void createEvent_success() {
        System.out.println("\n=== Starting createEvent_success test ===");
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        Event result = eventService.createEvent(event);

        assertNotNull(result);
        assertEquals(event, result);
        verify(eventRepository).save(any(Event.class));
        System.out.println("=== End of createEvent_success test ===\n");
    }

    @Test
    void deleteEvents_success() {
        System.out.println("\n=== Starting deleteEvents_success test ===");
        doNothing().when(eventRepository).deleteById(1L);

        eventService.deleteEvents(1L);

        verify(eventRepository).deleteById(1L);
        System.out.println("=== End of deleteEvents_success test ===\n");
    }
} 