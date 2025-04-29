package com.example.alumni.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class GroupMessage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long senderId;
    
    

    private Long groupId; // Used for group chats, null for one-on-one messages

    private String content;
    
    private LocalDateTime timestamp;

    public GroupMessage() {
        this.timestamp = LocalDateTime.now();
    }

    public GroupMessage(Long senderId,Long groupId, String content) {
        this.senderId = senderId;
    
        this.groupId = groupId;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }
}
