package com.user_messaging_system.message_service.service;

import com.user_messaging_system.message_service.api.input.MessageSendInput;

public interface MessageService {
    void sendMessage(MessageSendInput messageSendInput);
}