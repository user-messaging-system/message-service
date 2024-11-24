package com.user_messaging_system.message_service.api.output;

public record MessageUpdateOutput (
        String messageId,
        String senderId,
        String receiverId,
        String content,
        String receiveTime
){ }
