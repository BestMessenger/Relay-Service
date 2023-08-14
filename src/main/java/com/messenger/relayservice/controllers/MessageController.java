package com.messenger.relayservice.controllers;

import com.messenger.relayservice.dto.MessageRequestDTO;
import com.messenger.relayservice.dto.MessageResponseDTO;
import com.messenger.relayservice.models.MessageModel;
import com.messenger.relayservice.models.MessageKeyModel;
import com.messenger.relayservice.services.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @Operation(summary = "Save a message")
    @ApiResponse(responseCode = "201", description = "Message created",
            content = @Content(schema = @Schema(implementation = MessageResponseDTO.class)))
    @PostMapping
    public ResponseEntity<MessageResponseDTO> saveMessage(@RequestBody MessageRequestDTO requestDTO) {
        MessageModel message = convertRequestDTOToMessage(requestDTO);
        MessageModel savedMessage = messageService.saveMessage(message);
        MessageResponseDTO responseDTO = convertMessageToResponseDTO(savedMessage);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a message by IDs and timestamp")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message found",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Message not found")
    })
    @GetMapping("/{sentToId}/{sentFromId}/{group_id}")
    public ResponseEntity<MessageResponseDTO> getMessage(@PathVariable int sentToId,
                                                         @PathVariable int sentFromId,
                                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant timestamp,
                                                         @PathVariable("group_id") int groupId) {
        MessageKeyModel key = new MessageKeyModel(sentToId, sentFromId, timestamp, groupId);
        Optional<MessageModel> message = messageService.getMessage(key);
        return message.map(value -> new ResponseEntity<>(convertMessageToResponseDTO(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Get all messages")
    @ApiResponse(responseCode = "200", description = "List of messages",
            content = @Content(schema = @Schema(implementation = List.class)))
    @GetMapping
    public ResponseEntity<List<MessageResponseDTO>> getAllMessages() {
        Iterable<MessageModel> messages = messageService.getAllMessages();
        List<MessageResponseDTO> responseDTOs = convertMessagesToResponseDTOs(messages);
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }

    @Operation(summary = "Delete messages by group ID")
    @ApiResponse(responseCode = "204", description = "Messages deleted")
    @DeleteMapping("/group/{sent_from_id}/{sent_to_id}/{groupId}")
    public ResponseEntity<Void> deleteMessagesByGroupId(@PathVariable int groupId,
                                                        @PathVariable("sent_from_id") int sentFromId,
                                                        @PathVariable("sent_to_id") int sentToId) {
        messageService.deleteMessageByGroupId(sentToId, sentFromId, groupId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Get messages by group ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of messages",
                    content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "404", description = "Messages not found")
    })
    @GetMapping("/group/{sent_from_id}/{sent_to_id}/{groupId}")
    public ResponseEntity<List<MessageResponseDTO>> getMessageByGroupId(
            @PathVariable int groupId,
            @PathVariable("sent_from_id") int sentFromId,
            @PathVariable("sent_to_id") int sentToId) {

        List<MessageModel> message = messageService.getMessageByGroupId(sentToId, sentFromId, groupId);

        List<ResponseEntity<MessageResponseDTO>> responseList = message.stream()
                .map(value -> new ResponseEntity<>(convertMessageToResponseDTO(value), HttpStatus.OK))
                .collect(Collectors.toList());

        if (responseList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(responseList.stream()
                .map(ResponseEntity::getBody)
                .collect(Collectors.toList()));
    }

    private MessageModel convertRequestDTOToMessage(MessageRequestDTO requestDTO) {
        MessageModel messageModel = new MessageModel();
        MessageKeyModel key = new MessageKeyModel();
        key.setSent_to_id(requestDTO.getSentToId());
        key.setSent_from_id(requestDTO.getSentFromId());
        key.setTimestamp(requestDTO.getTimestamp().toInstant(ZoneOffset.UTC));
        key.setGroupId(requestDTO.getGroupId());

        messageModel.setKey(key);
        messageModel.setMessageId(requestDTO.getMessageId());
        messageModel.setContent_type(requestDTO.getContent_type());
        messageModel.setMedia_url(requestDTO.getMedia_url());

        return messageModel;
    }

    private MessageResponseDTO convertMessageToResponseDTO(MessageModel message) {
        MessageResponseDTO responseDTO = new MessageResponseDTO();
        responseDTO.setSentToId(message.getKey().getSent_to_id());
        responseDTO.setSentFromId(message.getKey().getSent_from_id());
        responseDTO.setTimestamp(message.getKey().getTimestamp().toString());
        responseDTO.setMessageId(message.getMessageId());
        responseDTO.setContent_type(message.getContent_type());
        responseDTO.setMedia_url(message.getMedia_url());
        responseDTO.setGroupId(message.getKey().getGroupId());

        return responseDTO;
    }

    private List<MessageResponseDTO> convertMessagesToResponseDTOs(Iterable<MessageModel> messages) {
        List<MessageResponseDTO> responseDTOs = new ArrayList<>();
        for (MessageModel message : messages) {
            responseDTOs.add(convertMessageToResponseDTO(message));
        }
        return responseDTOs;
    }
}
