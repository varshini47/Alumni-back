package com.example.alumni.dto;

import com.example.alumni.model.Connection;
import com.example.alumni.model.User;

import java.util.HashMap;
import java.util.Map;

public class ConnectionDTO {
    private Long id;
    private Map<String, Object> targetUser;
    private String status;

    public ConnectionDTO(Connection connection, User currentUser) {
        this.id = connection.getId();
        
        // Determine if the current user is the sender or receiver
        User targetUser = connection.getSender().getId().equals(currentUser.getId()) ? 
            connection.getReceiver() : connection.getSender();
        
        Map<String, Object> targetUserMap = new HashMap<>();
        targetUserMap.put("id", targetUser.getId());
        targetUserMap.put("name", targetUser.getName() + " " + targetUser.getLastName());
        targetUserMap.put("email", targetUser.getEmail());
        targetUserMap.put("imageUrl", targetUser.getImageUrl());
        
        this.targetUser = targetUserMap;
        
        // Map backend status to frontend status
        switch (connection.getStatus()) {
            case PENDING:
                this.status = "pending";
                break;
            case ACCEPTED:
                this.status = "connected";
                break;
            default:
                this.status = "rejected";
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<String, Object> getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(Map<String, Object> targetUser) {
        this.targetUser = targetUser;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
} 