package com.user_messaging_system.message_service.api.output;

import java.time.LocalDateTime;

public record MessageGetOutput(
        String senderId,
        String receiverId,
        String content,
        LocalDateTime receiveTime
) {}
