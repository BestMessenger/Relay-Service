package com.messenger.relayservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("messages")
public class MessageModel {

    @PrimaryKey
    private MessageKeyModel key;

    @Column
    private int messageId;

    @Column
    private String content_type;

    @Column
    private String media_url;
}
