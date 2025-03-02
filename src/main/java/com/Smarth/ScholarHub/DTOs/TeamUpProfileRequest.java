package com.Smarth.ScholarHub.DTOs;

import java.util.List;
import java.util.UUID;

public class TeamUpProfileRequest {

    private UUID userId;
    private String bio;
    private List<String> skills;
    private Boolean isAvailable;

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean available) {
        isAvailable = available;
    }

}