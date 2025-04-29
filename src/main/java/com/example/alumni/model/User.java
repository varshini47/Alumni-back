package com.example.alumni.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) 
    private String name;

    private String lastName;

    @Column(nullable = false, unique = true) 
    private String email;

    private String phone;
    private String role;
    private String batch;
    private String rollNo;
    private String profileType;

    public String getProfileType() {
        return profileType;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }
    @Column(nullable = false) 
    private String password;

    private String department;
    private String imageUrl;

    // One-to-Many relationship with WorkExperience
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // Prevents infinite recursion in JSON serialization
    private List<WorkExperience> workExperiences = new ArrayList<>();

    // Default constructor
    public User() {}

    // Parameterized constructor
    public User(Long id,String profileType, String name, String lastName, String email, String password, String role, 
                String phone, String batch, String rollNo, String department, String imageUrl) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.profileType=profileType;
        this.password = password;
        this.role = role;
        this.phone = phone;
        this.batch = batch;
        this.rollNo = rollNo;
        this.department = department;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getBatch() { return batch; }
    public void setBatch(String batch) { this.batch = batch; }

    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public List<WorkExperience> getWorkExperiences() { return workExperiences; }
    public void setWorkExperiences(List<WorkExperience> workExperiences) { this.workExperiences = workExperiences; }
}
