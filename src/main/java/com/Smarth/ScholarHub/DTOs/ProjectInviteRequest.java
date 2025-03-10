package com.Smarth.ScholarHub.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProjectInviteRequest {

    private UUID senderId;
    private String receiverEmail;
    private UUID projectId;
    private String inviteeRole;
    private String status;

}