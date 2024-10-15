package com.user_messaging_system.message_service.dto;

import java.time.LocalDateTime;

public record MessageDto(
        String id,
        String senderId,
        String receiverId,
        String content,
        LocalDateTime receiveTime
) {
}
