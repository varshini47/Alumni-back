package com.example.alumni.controller;

import com.example.alumni.model.Event;
import com.example.alumni.model.JobOpportunity;
import com.example.alumni.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:3000") // Enable CORS for frontend
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event savedEvent = eventService.createEvent(event);
        return ResponseEntity.ok(savedEvent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id) {
     
        eventService.deleteEvents(id);
        return ResponseEntity.ok().body("Job deleted successfully");
    }
}
