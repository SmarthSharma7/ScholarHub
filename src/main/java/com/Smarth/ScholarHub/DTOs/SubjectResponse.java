package com.Smarth.ScholarHub.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Setter
@Getter
public class SubjectResponse {

    private UUID id;
    private String name;
    private int totalClasses;
    private int attendedClasses;
    private double attendedPercentage;
    private Date createdAt;

}