package com.Smarth.ScholarHub.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "attendance_records", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "subject_id", "date"})
})
@Data
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne // Many subjects can belong to one user
    @JoinColumn(name = "user_id", nullable = false) // Foreign key column
    private User user;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private SubjectNew subjectNew;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "status")
    private String status;

    @Column(name = "note")
    private String note;

}
