package com.Smarth.ScholarHub.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
public class TodoTaskResponse {

    private UUID id;
    private String title;
    private UUID boardId;
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;

    private String priority;

}
