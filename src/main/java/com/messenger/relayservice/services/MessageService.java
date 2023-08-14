package com.messenger.relayservice.services;

import com.messenger.relayservice.models.MessageModel;
import com.messenger.relayservice.models.MessageKeyModel;
import com.messenger.relayservice.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public MessageModel saveMessage(MessageModel message) {
        return messageRepository.save(message);
    }

    public Optional<MessageModel> getMessage(MessageKeyModel key) {
        return messageRepository.findById(key);
    }

    public Iterable<MessageModel> getAllMessages() {
        return messageRepository.findAll();
    }

    public void deleteMessage(MessageKeyModel key) {
        messageRepository.deleteById(key);
    }

    public void deleteMessageByGroupId(int sentToId, int sentFromId, int groupId) {
        Instant startOf2021 = Instant.ofEpochMilli(1609459200000L);
        messageRepository.deleteBySentToIdAndSentFromIdAndTimestampGreaterThanAndGroupId(sentToId, sentFromId, startOf2021, groupId);
    }

    public List<MessageModel> getMessageByGroupId(int sentToId, int sentFromId, int groupId) {
        Instant startOf2021 = Instant.ofEpochMilli(1609459200000L);
        return messageRepository.findBySentToIdAndSentFromIdAndTimestampGreaterThanAndGroupId(sentToId, sentFromId, startOf2021, groupId);
    }
}
