package com.Smarth.ScholarHub.Services;

import com.Smarth.ScholarHub.DTOs.AllProjectsResponse;
import com.Smarth.ScholarHub.DTOs.SearchResponse;
import com.Smarth.ScholarHub.Models.Project;
import com.Smarth.ScholarHub.Models.ProjectMembers;
import com.Smarth.ScholarHub.Models.User;
import com.Smarth.ScholarHub.Repositories.ProjectMembersRepository;
import com.Smarth.ScholarHub.Repositories.ProjectRepository;
import com.Smarth.ScholarHub.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMembersRepository projectMembersRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProjectService(final ProjectRepository projectRepository,
                          final ProjectMembersRepository projectMembersRepository,
                          final UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.projectMembersRepository = projectMembersRepository;
        this.userRepository = userRepository;
    }

    public UUID createProject(Project project, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Couldn't find user"));
        projectRepository.save(project); // Save project first
        ProjectMembers projectMembers = new ProjectMembers();
        projectMembers.setProject(project);
        projectMembers.setUser(user);
        projectMembers.setRole("leader");
        projectMembersRepository.save(projectMembers); // Then save project member
        return project.getId();
    }

    public List<AllProjectsResponse> getAllProjectsByUserId(UUID userId) {
        List<AllProjectsResponse> projectsResponseList = new ArrayList<>();
        List<Project> projectList = projectMembersRepository.findProjectsByUserId(userId);
        for (Project project : projectList) {
            String role = "";
            List<ProjectMembers> tempList = projectMembersRepository.findByProjectId(project.getId());
            List<SearchResponse> membersList = new ArrayList<>();
            for (ProjectMembers projectMembers : tempList) {
                User user = projectMembers.getUser();
                if (user.getId().equals(userId)) role = projectMembers.getRole();
                SearchResponse searchResponse = new SearchResponse();
                searchResponse.setName(user.getName());
                searchResponse.setEmail(user.getEmail());
                searchResponse.setBio(user.getBio());
                searchResponse.setSkills(user.getSkills());
                searchResponse.setIsAvailable(user.getIsAvailable());
                membersList.add(searchResponse);
            }
            AllProjectsResponse allProjectsResponse = new AllProjectsResponse();
            allProjectsResponse.setId(project.getId());
            allProjectsResponse.setName(project.getName());
            allProjectsResponse.setDescription(project.getDescription());
            allProjectsResponse.setRole(role);
            allProjectsResponse.setMembers(membersList);
            projectsResponseList.add(allProjectsResponse);
        }
        return projectsResponseList;
    }

}