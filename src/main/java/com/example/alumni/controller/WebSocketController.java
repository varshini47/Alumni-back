package com.example.alumni.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.scheduling.annotation.Scheduled;

public class WebSocketController {
    @Autowired
    private SimpUserRegistry simpUserRegistry;

    @Scheduled(fixedRate = 5000) // Every 5 seconds, check connected users
    public void checkConnectedUsers() {
        try {
            System.out.println("👥 Connected Users: " + simpUserRegistry.getUsers());
        } catch (Exception e) {
            System.out.println("⚠️ Error checking connected users: " + e.getMessage());
        }
    }
}
