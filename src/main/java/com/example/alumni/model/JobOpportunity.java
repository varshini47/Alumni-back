package com.example.alumni.model;
import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "job_opportunities")
public class JobOpportunity {
    
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    private String title;
    private String company;
    private String location;
    private String jobType;
    private String experienceLevel;
    private String skills;
    private String description;
    private LocalDate applicationDeadline;
    private String applicationLink;
    private String contactInfo;
    private Long userId;
    public JobOpportunity() {
    }
    public JobOpportunity(Long id, String title, String company, String location, String jobType,
            String experienceLevel, String skills, String description, LocalDate applicationDeadline,
            String applicationLink, String contactInfo, Long userId) {
        this.id = id;
        this.title = title;
        this.company = company;
        this.location = location;
        this.jobType = jobType;
        this.experienceLevel = experienceLevel;
        this.skills = skills;
        this.description = description;
        this.applicationDeadline = applicationDeadline;
        this.applicationLink = applicationLink;
        this.contactInfo = contactInfo;
        this.userId = userId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getJobType() {
        return jobType;
    }
    public void setJobType(String jobType) {
        this.jobType = jobType;
    }
    public String getExperienceLevel() {
        return experienceLevel;
    }
    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }
    public String getSkills() {
        return skills;
    }
    public void setSkills(String skills) {
        this.skills = skills;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public LocalDate getApplicationDeadline() {
        return applicationDeadline;
    }
    public void setApplicationDeadline(LocalDate applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
    }
    public String getApplicationLink() {
        return applicationLink;
    }
    public void setApplicationLink(String applicationLink) {
        this.applicationLink = applicationLink;
    }
    public String getContactInfo() {
        return contactInfo;
    }
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }


}
