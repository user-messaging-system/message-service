package com.user_messaging_system.message_service.api.input;

import java.io.Serializable;
import java.time.LocalDateTime;

public class MessageSendInput implements Serializable{
    private static final long serialVersionUID = 1L;
    private final String senderId;
    private final String receiverId;
    private final String content;
    private LocalDateTime receiveTimestamp;

    public MessageSendInput(String senderId, String receiverId, String content){
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getReceiveTimestamp() {
        return receiveTimestamp;
    }

    public void setReceiveTimestamp(LocalDateTime receiveTimestamp) {
        this.receiveTimestamp = receiveTimestamp;
    }
}