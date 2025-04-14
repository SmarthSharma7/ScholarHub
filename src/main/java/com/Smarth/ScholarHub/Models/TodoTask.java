package com.Smarth.ScholarHub.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "todo_tasks")
@Data
public class TodoTask {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "title")
    private String title;

    @ManyToOne // Many tasks can belong to one user
    @JoinColumn(name = "user_id") // Foreign key column
    private User user;

    @ManyToOne // Many tasks can belong to one board
    @JoinColumn(name = "board_id") // Foreign key column
    private Board board;

    @Column(name = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "priority")
    private String priority;

}