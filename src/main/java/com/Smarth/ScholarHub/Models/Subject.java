
package com.Smarth.ScholarHub.Models;

import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne // Many subjects can belong to one user
    @JoinColumn(name = "user_id") // Foreign key column
    private User user;

    @Column(name = "name")
    private String name;

    @Column(name = "total_classes")
    private int totalClasses;

    @Column(name = "attended_classes")
    private int attendedClasses;

    @Column(name = "attendance_percentage", precision = 2, scale = 2, insertable = false, updatable = false)
    // Generated column
    private double attendancePercentage;

    // Getters and setters for all fields

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public double getAttendancePercentage() {
        return attendancePercentage;
    }

}