package com.Smarth.ScholarHub.Models;

import com.Smarth.ScholarHub.StringListConverter;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user_details")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password_hash")
    private String password;

    @Column(name = "created_at")
    private java.sql.Timestamp createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = Timestamp.from(Instant.now());
    }

    @Column(name = "bio")
    private String bio;

    @Convert(converter = StringListConverter.class)
    @Column(name = "skills", columnDefinition = "TEXT")
    private List<String> skills;

    @Column(name = "is_available", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isAvailable;

}