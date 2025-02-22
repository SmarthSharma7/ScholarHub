
package com.Smarth.ScholarHub.Services;

import com.Smarth.ScholarHub.DTOs.TodoTaskRequest;
import com.Smarth.ScholarHub.Models.Board;
import com.Smarth.ScholarHub.Models.TodoTask;
import com.Smarth.ScholarHub.Models.User;
import com.Smarth.ScholarHub.Repositories.BoardRepository;
import com.Smarth.ScholarHub.Repositories.TodoTaskRepository;
import com.Smarth.ScholarHub.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TodoService {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    TodoTaskRepository todoTaskRepository;

    @Autowired
    UserRepository userRepository;

    public Board addBoard(UUID userId, String boardName) {
        Board board = new Board();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        board.setName(boardName);
        board.setUser(user);
        boardRepository.save(board);
        return board;
    }

    public void updateBoard(UUID boardId, String boardName) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new RuntimeException("Couldn't find board"));
        board.setName(boardName);
        boardRepository.save(board);
    }

    public void deleteBoard(UUID boardId) {
        boardRepository.deleteById(boardId);
    }

    public TodoTask addTask(TodoTaskRequest todoTaskRequest) {
        TodoTask todoTask = new TodoTask();
        Board board = boardRepository.findById(todoTaskRequest.getBoardId()).orElseThrow(() -> new RuntimeException("No boardId"));
        todoTask.setBoard(board);
        todoTask.setUser(board.getUser());
        todoTask.setPriority(todoTaskRequest.getPriority());
        todoTask.setTitle(todoTaskRequest.getTitle());
        todoTask.setDueDate(todoTaskRequest.getDueDate());
        todoTaskRepository.save(todoTask);
        return todoTask;
    }

}
