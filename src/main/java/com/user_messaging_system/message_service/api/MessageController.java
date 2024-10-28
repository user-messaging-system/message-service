package com.user_messaging_system.message_service.api;

import com.user_messaging_system.message_service.api.input.MessageSendInput;
import com.user_messaging_system.message_service.api.output.MessageGetOutput;
import com.user_messaging_system.message_service.mapper.MessageMapper;
import com.user_messaging_system.message_service.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService){
        this.messageService = messageService;
    }

    @GetMapping("/conversations/{senderId}/{receiverId}")
    public ResponseEntity<List<MessageGetOutput>> getMessagesBetweenUsers(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable String senderId,
            @PathVariable String receiverId
    ){
        List<MessageGetOutput> response = MessageMapper.INSTANCE.messageDtoListToMessageGetOutputList(
                messageService.getMessagesBetweenUsers(jwtToken, senderId, receiverId)
        );
        return ResponseEntity.ok((response));
    }

    @PostMapping
    public void sendMessage(@RequestBody MessageSendInput messageSendInput){
        messageService.sendMessage(messageSendInput);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteById(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable String id
    ){
        return null;
    }
}