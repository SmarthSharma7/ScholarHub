
package com.Smarth.ScholarHub.Controllers;

import com.Smarth.ScholarHub.Models.Board;
import com.Smarth.ScholarHub.Services.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
