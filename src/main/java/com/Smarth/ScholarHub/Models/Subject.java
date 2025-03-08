package com.Smarth.ScholarHub.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "subjects")
@Data
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne // Many subjects can belong to one user
    @JoinColumn(name = "user_id", nullable = false) // Foreign key column
    private User user;

    @Column(name = "name")
    private String name;

    @Column(name = "total_classes")
    private int totalClasses;

    @Column(name = "attended_classes")
    private int attendedClasses;

    @Column(name = "created_at")
    private java.sql.Timestamp createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = Timestamp.from(Instant.now());
    }
}