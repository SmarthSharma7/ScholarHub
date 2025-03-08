package com.Smarth.ScholarHub.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TeamUpProfileResponse {

    private String bio;
    private List<String> skills;
    private Boolean isAvailable;

}