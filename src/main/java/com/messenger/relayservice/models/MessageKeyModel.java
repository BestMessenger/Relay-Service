package com.messenger.relayservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyClass
public class MessageKeyModel {
    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED)
    private Instant timestamp;
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    private int groupId;
}
