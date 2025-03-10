package com.Smarth.ScholarHub.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AllInvitesResponse {

    private UUID id;
    private SearchResponse sender;
    private SearchResponse receiver;
    private String projectName;
    private String projectDescription;
    private String inviteeRole;
    private String status;

}
