package com.Smarth.ScholarHub.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SearchResponse {

    private String name;
    private String email;
    private String bio;
    private List<String> skills;
    private Boolean isAvailable;

}