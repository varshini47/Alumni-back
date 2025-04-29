package com.example.alumni.dto;

import java.util.List;

public class InviteRequest {
    private Long userId;
    private String fromEmail;
    private String name;
    private List<String> toEmails;
    private String registrationLink;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public List<String> getToEmails() {
        return toEmails;
    }

    public void setToEmails(List<String> toEmails) {
        this.toEmails = toEmails;
    }

    public String getRegistrationLink() {
        return registrationLink;
    }

    public void setRegistrationLink(String registrationLink) {
        this.registrationLink = registrationLink;
    }
} 