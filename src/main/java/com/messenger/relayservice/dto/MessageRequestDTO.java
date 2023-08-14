package com.messenger.relayservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageRequestDTO {
    private int sentToId;
    private int sentFromId;
    private LocalDateTime timestamp;
    private int messageId;
    private String content_type;
    private String media_url;
    private int groupId;
}
