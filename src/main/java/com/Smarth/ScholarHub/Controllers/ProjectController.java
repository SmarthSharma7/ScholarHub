package com.Smarth.ScholarHub.Controllers;

import com.Smarth.ScholarHub.DTOs.AllInvitesResponse;
import com.Smarth.ScholarHub.DTOs.AllProjectsResponse;
import com.Smarth.ScholarHub.DTOs.MessageResponse;
import com.Smarth.ScholarHub.DTOs.ProjectInviteRequest;
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
            projectService.updateProjectInviteStatus(projectInviteId, status);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
        return ResponseEntity.ok(new MessageResponse("Invite status updated successfully"));
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

}