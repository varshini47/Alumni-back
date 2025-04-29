package com.example.alumni.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "achievements")
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate dateOfAchievement;

    @Column(nullable = false)
    private String category;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String supportingDocuments; // File path or URL

    private String organization;

    private String achievedBy;

  
    // Constructors
    public Achievement() {}

    public Achievement(String title, LocalDate dateOfAchievement,Long userId, String email,String category, String description, String supportingDocuments, String organization,String achievedBy) {
        this.title = title;
        this.dateOfAchievement = dateOfAchievement;
        this.userId=userId;
        this.category = category;
        this.description = description;
        this.supportingDocuments = supportingDocuments;
        this.organization = organization;
        this.achievedBy= achievedBy;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDateOfAchievement() {
        return dateOfAchievement;
    }

    public void setDateOfAchievement(LocalDate dateOfAchievement) {
        this.dateOfAchievement = dateOfAchievement;
    }

    public String getCategory() {
        return category;
    }


    public String getAchievedBy() {
        return achievedBy;
    }

    public void setAchievedBy(String achievedBy) {
        this.achievedBy = achievedBy;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSupportingDocuments() {
        return supportingDocuments;
    }

    public void setSupportingDocuments(String supportingDocuments) {
        this.supportingDocuments = supportingDocuments;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }
}

