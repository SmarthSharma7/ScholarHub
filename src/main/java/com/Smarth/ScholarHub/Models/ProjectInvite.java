package com.Smarth.ScholarHub.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "project_invites")
@Data
public class ProjectInvite {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "invitee_role")
    private String inviteeRole;

    @Column(name = "status")
    private String status;

}
