package com.messenger.relayservice.repositories;

import com.messenger.relayservice.models.MessageKeyModel;
import com.messenger.relayservice.models.MessageModel;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<MessageModel, MessageKeyModel> {
    @Transactional
    @Query("SELECT * FROM messages WHERE sent_to_id = :sentToId AND sent_from_id = :sentFromId AND timestamp > :timestamp AND groupId = :groupId")
    List<MessageModel> findBySentToIdAndSentFromIdAndTimestampGreaterThanAndGroupId(int sentToId, int sentFromId, Instant timestamp, int groupId);

    @Transactional
    @Query("DELETE FROM messages " +
            "WHERE sent_to_id = :sentToId " +
            "AND sent_from_id = :sentFromId " +
            "AND timestamp > :timestamp " +
            "AND groupId = :groupId")
    void deleteBySentToIdAndSentFromIdAndTimestampGreaterThanAndGroupId(
            int sentToId, int sentFromId, Instant timestamp, int groupId);
}
