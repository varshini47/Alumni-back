package com.example.alumni.dto;

import java.time.LocalDate;

public class AchievementDTO {
    private Long id;
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

    private String title;
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private String category;
    public LocalDate getDateOfAchievement() {
        return dateOfAchievement;
    }

    public void setDateOfAchievement(LocalDate dateOfAchievement) {
        this.dateOfAchievement = dateOfAchievement;
    }

    private LocalDate dateOfAchievement;
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;
    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    private String organization;
    public String getSupportingDocuments() {
        return supportingDocuments;
    }

    public void setSupportingDocuments(String supportingDocuments) {
        this.supportingDocuments = supportingDocuments;
    }

    private String supportingDocuments;
    public String getAlumniName() {
        return alumniName;
    }

    public void setAlumniName(String alumniName) {
        this.alumniName = alumniName;
    }

    private String alumniName;

    public AchievementDTO() {
    }

    public AchievementDTO(Long id, String title, String category, LocalDate dateOfAchievement, String description, String organization, String supportingDocuments, String alumniName) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.dateOfAchievement = dateOfAchievement;
        this.description = description;
        this.organization = organization;
        this.supportingDocuments = supportingDocuments;
        this.alumniName = alumniName;
    }

    // Getters and Setters
}
