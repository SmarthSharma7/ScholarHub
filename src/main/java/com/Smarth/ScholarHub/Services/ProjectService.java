package com.Smarth.ScholarHub.Services;

import com.Smarth.ScholarHub.DTOs.AllInvitesResponse;
import com.Smarth.ScholarHub.DTOs.AllProjectsResponse;
import com.Smarth.ScholarHub.DTOs.ProjectInviteRequest;
import com.Smarth.ScholarHub.DTOs.SearchResponse;
import com.Smarth.ScholarHub.Models.Project;
import com.Smarth.ScholarHub.Models.ProjectInvite;
import com.Smarth.ScholarHub.Models.ProjectMembers;
import com.Smarth.ScholarHub.Models.User;
import com.Smarth.ScholarHub.Repositories.ProjectInvitesRepository;
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
    private final ProjectInvitesRepository projectInvitesRepository;

    @Autowired
    public ProjectService(final ProjectRepository projectRepository,
                          final ProjectMembersRepository projectMembersRepository,
                          final UserRepository userRepository,
                          final ProjectInvitesRepository projectInvitesRepository) {
        this.projectRepository = projectRepository;
        this.projectMembersRepository = projectMembersRepository;
        this.userRepository = userRepository;
        this.projectInvitesRepository = projectInvitesRepository;
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

    public void sendProjectInvite(ProjectInviteRequest projectInviteRequest) {
        User sender = userRepository.findById(projectInviteRequest.getSenderId())
                .orElseThrow(() -> new RuntimeException("Couldn't find receiver"));
        User receiver = userRepository.findByEmail(projectInviteRequest.getReceiverEmail())
                .orElseThrow(() -> new RuntimeException("Couldn't find receiver"));
        Project project = projectRepository.findById(projectInviteRequest.getProjectId())
                .orElseThrow(() -> new RuntimeException("Couldn't find project"));
        ProjectInvite projectInvite = new ProjectInvite();
        projectInvite.setSender(sender);
        projectInvite.setReceiver(receiver);
        projectInvite.setProject(project);
        projectInvite.setInviteeRole(projectInviteRequest.getInviteeRole());
        projectInvite.setStatus(projectInviteRequest.getStatus());
        projectInvitesRepository.save(projectInvite);
    }

    public List<AllInvitesResponse> getAllInvitesByUserId(UUID userId, String senderAlso) {
        List<AllInvitesResponse> allInvites = new ArrayList<>();
        List<ProjectInvite> invitesList;
        invitesList = projectInvitesRepository.findBySenderIdOrReceiverId(userId, userId);

        for (ProjectInvite invite : invitesList) {
            AllInvitesResponse allInviteResponse = new AllInvitesResponse();

            Project project = projectRepository.findById(invite.getProject().getId()).
                    orElseThrow(() -> new RuntimeException("Couldn't find project"));

            User sender = invite.getSender();
            SearchResponse searchResponseSender = new SearchResponse();
            searchResponseSender.setName(sender.getName());
            searchResponseSender.setEmail(sender.getEmail());
            searchResponseSender.setBio(sender.getBio());
            searchResponseSender.setSkills(sender.getSkills());
            searchResponseSender.setIsAvailable(sender.getIsAvailable());

            User receiver = invite.getReceiver();
            SearchResponse searchResponseReceiver = new SearchResponse();
            searchResponseReceiver.setName(receiver.getName());
            searchResponseReceiver.setEmail(receiver.getEmail());
            searchResponseReceiver.setBio(receiver.getBio());
            searchResponseReceiver.setSkills(receiver.getSkills());
            searchResponseReceiver.setIsAvailable(receiver.getIsAvailable());

            allInviteResponse.setId(invite.getId());
            allInviteResponse.setSender(searchResponseSender);
            allInviteResponse.setReceiver(searchResponseReceiver);
            allInviteResponse.setProjectId(project.getId());
            allInviteResponse.setProjectName(project.getName());
            allInviteResponse.setProjectDescription(project.getDescription());
            allInviteResponse.setInviteeRole(invite.getInviteeRole());
            allInviteResponse.setStatus(invite.getStatus());

            allInvites.add(allInviteResponse);
        }
        return allInvites;
    }

    public List<SearchResponse> updateProjectInviteStatus(UUID projectInviteId, String status) {
        ProjectInvite projectInvite = projectInvitesRepository.findById(projectInviteId).
                orElseThrow(() -> new RuntimeException("Couldn't find project invite"));
        projectInvite.setStatus(status);
        projectInvitesRepository.save(projectInvite);
        List<SearchResponse> membersToSend = new ArrayList<>();
        if (status.equals("accepted")) {

            boolean alreadyExists = projectMembersRepository
                    .existsByProjectIdAndUserId(projectInvite.getProject().getId(), projectInvite.getReceiver().getId());

            if (!alreadyExists) {
                ProjectMembers projectMembers = new ProjectMembers();
                projectMembers.setProject(projectInvite.getProject());
                projectMembers.setUser(projectInvite.getReceiver());
                projectMembers.setRole("member");
                projectMembersRepository.save(projectMembers);
            }

            Project project = projectInvite.getProject();
            List<ProjectMembers> members = projectMembersRepository.findByProjectId(project.getId());
            for (ProjectMembers member : members) {
                User user = member.getUser();
                SearchResponse searchResponse = new SearchResponse();
                searchResponse.setName(user.getName());
                searchResponse.setEmail(user.getEmail());
                searchResponse.setBio(user.getBio());
                searchResponse.setSkills(user.getSkills());
                searchResponse.setIsAvailable(user.getIsAvailable());
                membersToSend.add(searchResponse);
            }
        }
        return membersToSend;
    }

    public void deleteProjectInvite(UUID projectInviteId) {
        projectInvitesRepository.deleteById(projectInviteId);
    }

}