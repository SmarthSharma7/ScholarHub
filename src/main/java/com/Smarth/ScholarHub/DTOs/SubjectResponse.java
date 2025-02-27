package com.Smarth.ScholarHub.DTOs;

import java.util.Date;
import java.util.UUID;

public class SubjectResponse {

    private UUID id;
    private String name;
    private int totalClasses;
    private int attendedClasses;
    private double attendedPercentage;
    private Date createdAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalClasses() {
        return totalClasses;
    }

    public void setTotalClasses(int totalClasses) {
        this.totalClasses = totalClasses;
    }

    public int getAttendedClasses() {
        return attendedClasses;
    }

    public void setAttendedClasses(int attendedClasses) {
        this.attendedClasses = attendedClasses;
    }

    public double getAttendedPercentage() {
        return attendedPercentage;
    }

    public void setAttendedPercentage(double attendedPercentage) {
        this.attendedPercentage = attendedPercentage;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}