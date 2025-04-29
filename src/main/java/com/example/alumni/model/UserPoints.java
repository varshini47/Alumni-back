package com.example.alumni.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_points")
public class UserPoints {

    @Id
    private Long userId; // Using userId as the primary key
    
    @Column(nullable = false)
    private int points;

    public UserPoints() {}

    public UserPoints(Long userId, int points) {
        this.userId = userId;
        this.points = points;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addPoints(int additionalPoints) {
        this.points += additionalPoints;
    }
}
