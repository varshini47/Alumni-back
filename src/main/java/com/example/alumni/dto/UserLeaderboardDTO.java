package com.example.alumni.dto;

import com.example.alumni.model.User;

public class UserLeaderboardDTO {
    private Long id;
    private String name;
    private String imageUrl;
    private String email;
    private String phone;
    private int points;
    private String role;
    private String batch;

    public UserLeaderboardDTO(User user, int points) {
        this.id = user.getId();
        this.name = user.getName() + " " + user.getLastName();
        this.imageUrl = user.getImageUrl();
        this.email=user.getEmail();
        this.phone=user.getPhone();
        this.points = points;
        this.role=user.getRole();
        this.batch=user.getBatch();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public int getPoints() { return points; }
    public String getRole() {return role;}
    public String getBatch(){return batch;}
}
