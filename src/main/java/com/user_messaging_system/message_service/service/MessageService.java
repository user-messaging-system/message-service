package com.user_messaging_system.message_service.service;

import com.user_messaging_system.message_service.api.input.MessageSendInput;
import com.user_messaging_system.message_service.api.input.MessageUpdateInput;
import com.user_messaging_system.message_service.dto.MessageDto;

import java.util.List;

public interface MessageService {
    void sendMessage(MessageSendInput messageSendInput);
    List<MessageDto> getMessagesBetweenUsers(String jwtToken, String senderId, String receiverId);
    void deleteById(String jwtToken, String id);
    MessageDto updateMessageById(String messageId, String jwtToken, MessageUpdateInput messageUpdateInput);
}