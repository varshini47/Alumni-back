package com.example.alumni.controller;

import com.example.alumni.model.Connection;
import com.example.alumni.service.ConnectionService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/connections")
@CrossOrigin(origins = "http://localhost:3000") // Enable CORS for frontend
public class ConnectionController {
    private final ConnectionService connectionService;

    public ConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @PostMapping("/send/{senderId}/{receiverId}")
    public Connection sendRequest(@PathVariable Long senderId, @PathVariable Long receiverId) {
        return connectionService.sendRequest(senderId, receiverId);
    }

    // @GetMapping("/pending/{userId}")
    // public List<Connection> getPendingRequests(@PathVariable Long userId) {
    //     return connectionService.getPendingRequests(userId);
    // }

    // @GetMapping("/accepted/{userId}")
    // public List<Connection> getConnectedUsers(@PathVariable Long userId) {
    //     return connectionService.getConnectedUsers(userId);
    // }
    @GetMapping("/pending/{userId}")
    public ResponseEntity<?> getPendingRequests(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(connectionService.getPendingRequests(userId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/accepted/{userId}")
    public ResponseEntity<?> getConnectedUsers(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(connectionService.getConnectedUsers(userId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping("/accept/{requestId}")
    public void acceptRequest(@PathVariable Long requestId) {
        System.out.println("\n\n........acceptRequest\n\n");
        connectionService.acceptRequest(requestId);
    }

    @DeleteMapping("/reject/{requestId}")
    public void rejectRequest(@PathVariable Long requestId) {
        connectionService.rejectRequest(requestId);
    }
    @GetMapping("/user/{userId}")
    public ConnectionResponse getConnections(@PathVariable Long userId) {
        List<Connection> pendingRequests = connectionService.getPendingConnections(userId);
        List<Connection> acceptedConnections = connectionService.getConnectedUsers(userId);

        return new ConnectionResponse(pendingRequests, acceptedConnections);
    }
    
    // DTO to return both pending and accepted connections
    public static class ConnectionResponse {
        private List<Connection> pending;
        private List<Connection> accepted;

        public ConnectionResponse(List<Connection> pending, List<Connection> accepted) {
            this.pending = pending;
            this.accepted = accepted;
        }

        public List<Connection> getPending() {
            return pending;
        }

        public List<Connection> getAccepted() {
            return accepted;
        }
    }
}
