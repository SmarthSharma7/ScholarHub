package com.Smarth.ScholarHub.Controllers;

import com.Smarth.ScholarHub.DTOs.*;
import com.Smarth.ScholarHub.Models.Project;
import com.Smarth.ScholarHub.Services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(final ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/create/{userId}")
    public ResponseEntity<?> createProject(@PathVariable("userId") UUID userId, @RequestBody Project project) {
        try {
            String id = projectService.createProject(project, userId).toString();
            return ResponseEntity.ok(new MessageResponse(id));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
    }

    @GetMapping("/getAll/{userId}")
    public ResponseEntity<List<AllProjectsResponse>> getAllProjectsByUserId(@PathVariable UUID userId) {
        List<AllProjectsResponse> allProjects = projectService.getAllProjectsByUserId(userId);
        return ResponseEntity.ok(allProjects);
    }

    @PostMapping("/invite/send")
    public ResponseEntity<?> sendProjectInvite(@RequestBody ProjectInviteRequest projectInviteRequest) {
        try {
            projectService.sendProjectInvite(projectInviteRequest);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
        return ResponseEntity.ok(new MessageResponse("Invite sent successfully"));
    }

    @GetMapping("/invite/getAll/{userId}/{senderAlso}")
    public ResponseEntity<List<AllInvitesResponse>> getAllInvitesByUserId(@PathVariable("userId") UUID userId,
                                                                          @PathVariable("senderAlso") String senderAlso) {
        return ResponseEntity.ok(projectService.getAllInvitesByUserId(userId, senderAlso));
    }

    @PostMapping("/invite/updateStatus/{projectInviteId}/{status}")
    public ResponseEntity<?> updateProjectInviteStatus(@PathVariable("projectInviteId") UUID projectInviteId,
                                                       @PathVariable("status") String status) {
        try {
            List<SearchResponse> members = projectService.updateProjectInviteStatus(projectInviteId, status);
            return ResponseEntity.ok(members);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
    }

    @DeleteMapping("/invite/delete/{projectInviteId}")
    public ResponseEntity<?> deleteProjectInvite(@PathVariable("projectInviteId") UUID projectInviteId) {
        try {
            projectService.deleteProjectInvite(projectInviteId);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
        return ResponseEntity.ok(new MessageResponse("Invite deleted successfully"));
    }

    @PutMapping("/editProject")
    public ResponseEntity<?> editProject(@RequestBody EditProjectRequest editProjectRequest) {
        try {
            projectService.editProject(editProjectRequest);
            return ResponseEntity.ok(new MessageResponse("Project updated successfully"));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/kickMember/{projectId}/{userEmail}")
    public ResponseEntity<?> kickMember(@PathVariable("projectId") UUID projectId,
                                        @PathVariable("userEmail") String userEmail) {
        String deleteProject;
        try {
            deleteProject = projectService.kickMember(projectId, userEmail);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
        return ResponseEntity.ok(new MessageResponse(deleteProject));
    }

    @PutMapping("/changeLeader/{projectId}/{oldLeaderEmail}/{newLeaderEmail}")
    public ResponseEntity<?> changeLeader(@PathVariable("projectId") UUID projectId,
                                          @PathVariable("oldLeaderEmail") String oldLeaderEmail,
                                          @PathVariable("newLeaderEmail") String newLeaderEmail) {
        try {
            projectService.changeLeader(projectId, oldLeaderEmail, newLeaderEmail);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
        return ResponseEntity.ok(new MessageResponse("Leader changed successfully"));
    }

    @DeleteMapping("/deleteProject/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable("projectId") UUID projectId) {
        try {
            projectService.deleteProject(projectId);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
        return ResponseEntity.ok(new MessageResponse("Project deleted successfully"));
    }

    @GetMapping("/messages/getAll/{userId}")
    public ResponseEntity<List<MessagesResponse>> getAllMessagesForUser(@PathVariable("userId") UUID userId) {
        return ResponseEntity.ok(projectService.getAllMessagesForUser(userId));
    }

    @PostMapping("/messages/send")
    public ResponseEntity<?> sendMessage(@RequestBody SendMessageRequest sendMessageRequest) {
        UUID id;
        try {
           id = projectService.sendMessage(sendMessageRequest);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
        return ResponseEntity.ok(new MessageResponse(id.toString()));
    }

    @GetMapping("/projectTasks/getAll/{userId}")
    public ResponseEntity<List<AllProjectTasksResponse>> getAllProjectTasksForUser(@PathVariable("userId") UUID userId) {
        return ResponseEntity.ok(projectService.getAllProjectTasksForUser(userId));
    }

    @PostMapping("/projectTasks/addTask")
    public ResponseEntity<?> addTask(@RequestBody AddTaskRequest addTaskRequest) {
        String id;
        try {
            id = projectService.addTask(addTaskRequest).toString();
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
        return ResponseEntity.ok(new MessageResponse("Task added successfully"));
    }

    @PutMapping("/projectTasks/updateStatus/{taskId}/{status}")
    public ResponseEntity<?> updateStatusOfProjectTask(@PathVariable("taskId") UUID taskId,
                                                       @PathVariable("status") String status) {
        try {
            projectService.updateStatusOfProjectTask(taskId, status);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
        return ResponseEntity.ok(new MessageResponse("Task status updated successfully"));
    }

    @DeleteMapping("/projectTasks/deleteTask/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable("taskId") UUID taskId) {
        try {
            projectService.deleteTask(taskId);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
        return ResponseEntity.ok(new MessageResponse("Task deleted successfully"));
    }

}