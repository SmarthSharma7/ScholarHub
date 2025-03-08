package com.Smarth.ScholarHub.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class TeamUpProfileRequest {

    private UUID userId;
    private String bio;
    private List<String> skills;
    private Boolean isAvailable;

}