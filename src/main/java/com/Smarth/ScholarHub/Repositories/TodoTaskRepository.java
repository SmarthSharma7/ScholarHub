
package com.Smarth.ScholarHub.Repositories;

import com.Smarth.ScholarHub.Models.TodoTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TodoTaskRepository extends JpaRepository<TodoTask, UUID> {

    List<TodoTask> findByBoardId(UUID boardId);// Get all to-do tasks for a board

    List<TodoTask> findByUserId(UUID userId);// Get all to-do tasks for a user

}