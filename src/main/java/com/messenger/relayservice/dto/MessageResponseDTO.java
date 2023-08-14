package com.messenger.relayservice.dto;

import lombok.Data;

@Data
public class MessageResponseDTO {
    private int sentToId;
    private int sentFromId;
    private String timestamp;
    private int messageId;
    private String content_type;
    private String media_url;
    private int groupId;
}
