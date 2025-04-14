package com.Smarth.ScholarHub.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class SendMessageRequest {

    private UUID projectId;
    private String messageContent;
    private String sentBy;
    private LocalDateTime sentAt;

}
