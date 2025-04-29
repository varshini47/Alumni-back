package com.example.alumni.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "work_experience")
public class WorkExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private boolean isPresent;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)  // JPA will create `user_id` column
    private User user;

    private LocalDate startDate;
    private LocalDate endDate;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String location;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Constructors
    public WorkExperience() {}

    public WorkExperience(LocalDate startDate, LocalDate endDate, boolean isPresent, User user, String company, String role, String location, String description) {
        System.out.println(isPresent);
        this.startDate = startDate;
        // this.endDate = endDate;
        this.endDate = isPresent ? null : endDate;  // If isPresent is true, endDate is null
        this.isPresent = isPresent;
        this.user = user;
        this.company = company;
        this.role = role;
        this.location = location;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setIsPresent(boolean current) {
        isPresent = current;
        if (current) {
            this.endDate = null;  // If current, set endDate to null
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
