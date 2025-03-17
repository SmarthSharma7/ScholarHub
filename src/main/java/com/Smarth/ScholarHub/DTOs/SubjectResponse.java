package com.Smarth.ScholarHub.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Builder
public class SubjectResponse {

    private UUID id;
    private String name;
    private int totalClasses;
    private int attendedClasses;
    private double attendedPercentage;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private Date createdAt;

    private List<SubjectRecord> subjectRecord;

}