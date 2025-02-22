
package com.Smarth.ScholarHub.Controllers;

import com.Smarth.ScholarHub.DTOs.TodoTaskRequest;
import com.Smarth.ScholarHub.Models.Board;
import com.Smarth.ScholarHub.Models.TodoTask;
import com.Smarth.ScholarHub.Services.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/todo")
public class TodoController {

    @Autowired
    TodoService todoService;

    @PostMapping("/boards/add/{userId}/{boardName}")
    public ResponseEntity<?> addBoard(@PathVariable("userId") UUID userId,
                                      @PathVariable("boardName") String boardName) {
        Board board;
        try {
            board = todoService.addBoard(userId, boardName);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("{\"message\": \"" + ex.getMessage() + "\"}");
        }
        return ResponseEntity.ok(board);
    }

    @PutMapping("/boards/update/{boardId}/{boardName}")
    public ResponseEntity<?> updateBoard(@PathVariable("boardId") UUID boardId,
                                         @PathVariable("boardName") String boardName) {
        try {
            todoService.updateBoard(boardId, boardName);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("{\"message\": \"" + ex.getMessage() + "\"}");
        }
        return ResponseEntity.ok("{\"message\": \"Board updated successfully\"}");
    }

    @DeleteMapping("/boards/delete/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable("boardId") UUID boardId) {
        try {
            todoService.deleteBoard(boardId);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("{\"message\": \"" + ex.getMessage() + "\"}");
        }
        return ResponseEntity.ok("{\"message\": \"Board deleted successfully\"}");
    }

    @PostMapping("task/add")
    public ResponseEntity<?> addTodoTask(@RequestBody TodoTaskRequest todoTaskRequest) {
        TodoTask todoTask;
        try {
            todoTask = todoService.addTask(todoTaskRequest);
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"" + ex.getMessage() + "\"}");
        }
        return ResponseEntity.ok(todoTask);
    }

}