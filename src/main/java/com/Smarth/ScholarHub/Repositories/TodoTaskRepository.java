
package com.Smarth.ScholarHub.Repositories;

import com.Smarth.ScholarHub.Models.TodoTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TodoTaskRepository extends JpaRepository<TodoTask, UUID> {

    List<TodoTask> findByBoardId(UUID boardId);// Get all to-do tasks for a board

    List<TodoTask> findByUserId(UUID userId);// Get all to-do tasks for a user

}