package com.Smarth.ScholarHub.Services;

import com.Smarth.ScholarHub.DTOs.*;
import com.Smarth.ScholarHub.Models.*;
import com.Smarth.ScholarHub.Repositories.*;
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
    private final MessageRepository messageRepository;
    private final ProjectTaskRepository projectTaskRepository;

    @Autowired
    public ProjectService(final ProjectRepository projectRepository,
                          final ProjectMembersRepository projectMembersRepository,
                          final UserRepository userRepository,
                          final ProjectInvitesRepository projectInvitesRepository, MessageRepository messageRepository, ProjectTaskRepository projectTaskRepository) {
        this.projectRepository = projectRepository;
        this.projectMembersRepository = projectMembersRepository;
        this.userRepository = userRepository;
        this.projectInvitesRepository = projectInvitesRepository;
        this.messageRepository = messageRepository;
        this.projectTaskRepository = projectTaskRepository;
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
            String leaderEmail = "";
            List<ProjectMembers> tempList = projectMembersRepository.findByProjectId(project.getId());
            List<SearchResponse> membersList = new ArrayList<>();
            for (ProjectMembers projectMembers : tempList) {
                User user = projectMembers.getUser();
                if (user.getId().equals(userId)) role = projectMembers.getRole();
                if (projectMembers.getRole().equals("leader")) leaderEmail = user.getEmail();
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
            allProjectsResponse.setLeaderEmail(leaderEmail);
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

    public void editProject(EditProjectRequest editProjectRequest) {
        Project project = projectRepository.findById(editProjectRequest.getId()).
                orElseThrow(() -> new RuntimeException("Project not found"));
        project.setName(editProjectRequest.getName());
        project.setDescription(editProjectRequest.getDescription());
        projectRepository.save(project);
    }

    public String kickMember(UUID projectId, String userEmail) {
        UUID userId = userRepository.findByEmail(userEmail).get().getId();
        ProjectMembers projectMember = projectMembersRepository.findByProjectIdAndUserId(projectId, userId);
        if (projectMember == null) throw new RuntimeException("Member not found in this project.");
        projectMembersRepository.delete(projectMember);
        List<ProjectMembers> projectMembers = projectMembersRepository.findByProjectId(projectId);
        if (projectMembers.isEmpty()) {
            projectRepository.deleteById(projectId);
            return "true";
        }
        return "false";
    }

    public void changeLeader(UUID projectId, String oldLeaderEmail, String newLeaderEmail) {
        UUID oldLeaderId = userRepository.findByEmail(oldLeaderEmail).get().getId();
        UUID newLeaderId = userRepository.findByEmail(newLeaderEmail).get().getId();
        ProjectMembers oldLeader = projectMembersRepository.findByProjectIdAndUserId(projectId, oldLeaderId);
        oldLeader.setRole("member");
        projectMembersRepository.save(oldLeader);
        ProjectMembers newLeader = projectMembersRepository.findByProjectIdAndUserId(projectId, newLeaderId);
        newLeader.setRole("leader");
        projectMembersRepository.save(newLeader);
    }

    public void deleteProject(UUID projectId) {
        projectRepository.deleteById(projectId);
    }

    public List<MessagesResponse> getAllMessagesForUser(UUID userId) {
        List<MessagesResponse> messagesResponseList = new ArrayList<>();
        List<Project> projectList = projectMembersRepository.findProjectsByUserId(userId);
        for (Project project : projectList) {
            List<Message> messagesForProject = messageRepository.findByProjectId(project.getId());
            for (Message message : messagesForProject) {
                MessagesResponse messagesResponse = new MessagesResponse();
                messagesResponse.setId(message.getId());
                messagesResponse.setProjectId(message.getProject().getId());
                messagesResponse.setMessageContent(message.getMessageContent());
                messagesResponse.setSentBy(message.getSentBy().getEmail());
                messagesResponse.setSentAt(message.getSentAt());
                messagesResponseList.add(messagesResponse);
            }
        }
        return messagesResponseList;
    }

    public UUID sendMessage(SendMessageRequest sendMessageRequest) {
        Project project = projectRepository.findById(sendMessageRequest.getProjectId()).
                orElseThrow(() -> new RuntimeException("Couldn't find project"));
        User user = userRepository.findByEmail(sendMessageRequest.getSentBy())
                .orElseThrow(() -> new RuntimeException("Couldn't find user"));
        Message message = new Message();
        message.setProject(project);
        message.setMessageContent(sendMessageRequest.getMessageContent());
        message.setSentBy(user);
        messageRepository.save(message);
        return message.getId();
    }

    public List<AllProjectTasksResponse> getAllProjectTasksForUser(UUID userId) {
        List<AllProjectTasksResponse> allProjectTasksResponseList = new ArrayList<>();
        List<Project> projectList = projectMembersRepository.findProjectsByUserId(userId);
        for (Project project : projectList) {
            List<ProjectTask> projectTaskList = projectTaskRepository.findByProjectId(project.getId());
            for (ProjectTask projectTask : projectTaskList) {
                AllProjectTasksResponse allProjectTasksResponse = new AllProjectTasksResponse();
                allProjectTasksResponse.setId(projectTask.getId());
                allProjectTasksResponse.setProjectId(project.getId());
                allProjectTasksResponse.setAssignedTo(projectTask.getAssignedTo().getEmail());
                allProjectTasksResponse.setTitle(projectTask.getTitle());
                allProjectTasksResponse.setDescription(projectTask.getDescription());
                allProjectTasksResponse.setStatus(projectTask.getStatus());
                allProjectTasksResponse.setDueDate(projectTask.getDueDate());
                allProjectTasksResponse.setCreatedAt(projectTask.getCreatedAt());
                allProjectTasksResponseList.add(allProjectTasksResponse);
            }
        }
        return allProjectTasksResponseList;
    }

    public UUID addTask(AddTaskRequest addTaskRequest) {
        Project project = projectRepository.findById(addTaskRequest.getProjectId()).
                orElseThrow(() -> new RuntimeException("Couldn't find project"));
        User assignedTo = userRepository.findByEmail(addTaskRequest.getAssignedTo()).
                orElseThrow(() -> new RuntimeException("Couldn't find user"));
        ProjectTask projectTask = new ProjectTask();
        projectTask.setProject(project);
        projectTask.setAssignedTo(assignedTo);
        projectTask.setTitle(addTaskRequest.getTitle());
        projectTask.setDescription(addTaskRequest.getDescription());
        projectTask.setStatus(addTaskRequest.getStatus());
        projectTask.setDueDate(addTaskRequest.getDueDate());
        projectTaskRepository.save(projectTask);
        return projectTask.getId();
    }

    public void updateStatusOfProjectTask(UUID taskId, String status) {
        ProjectTask projectTask = projectTaskRepository.findById(taskId).
                orElseThrow(() -> new RuntimeException("Couldn't find task"));
        projectTask.setStatus(status);
        projectTaskRepository.save(projectTask);
    }
    public void deleteTask(UUID taskId) {
        ProjectTask projectTask = projectTaskRepository.findById(taskId).
                orElseThrow(() -> new RuntimeException("Couldn't find task"));
        projectTaskRepository.delete(projectTask);
    }

}