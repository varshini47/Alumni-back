package com.example.alumni.service;

import com.example.alumni.model.Event;
import com.example.alumni.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public void deleteEvents(Long id) {
        eventRepository.deleteById(id);
    }
}
