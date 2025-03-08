package com.Smarth.ScholarHub.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class AllProjectsResponse {

    private UUID id;
    private String name;
    private String description;
    private String role;
    private List<SearchResponse> members;

}
