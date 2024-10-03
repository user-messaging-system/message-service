package com.user_messaging_system.message_service.api.input;

import java.time.LocalDateTime;

public record MessageSendInput(
    String senderId,
    String receiverId,
    String context,
    LocalDateTime timestamp
){}