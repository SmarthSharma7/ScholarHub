package com.Smarth.ScholarHub.Controllers;

import com.Smarth.ScholarHub.DTOs.AllProjectsResponse;
import com.Smarth.ScholarHub.DTOs.MessageResponse;
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

}