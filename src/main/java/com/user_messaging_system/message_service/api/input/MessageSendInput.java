package com.user_messaging_system.message_service.api.input;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.time.LocalDateTime;

import static com.user_messaging_system.core_library.common.constant.ValidationConstant.*;

@Validated
public class MessageSendInput implements Serializable{
    private static final long serialVersionUID = 1L;
    @NotBlank(message = SENDER_ID_NOT_BLANK)
    @Size(min = UUID_LENGTH, max = UUID_LENGTH, message = "Invalid sender ID")
    private final String senderId;
    @NotBlank(message = RECEIVER_ID_NOT_BLANK)
    @Size(min = UUID_LENGTH, max = UUID_LENGTH, message = "Invalid receiver ID")
    private final String receiverId;
    @NotNull(message = MESSAGE_CONTENT_NULL)
    @Size(min = MESSAGE_CONTENT_MIN_LENGTH, max = MESSAGE_CONTENT_MAX_LENGTH, message = INVALID_MESSAGE_CONTENT)
    private final String content;
    @JsonIgnore
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