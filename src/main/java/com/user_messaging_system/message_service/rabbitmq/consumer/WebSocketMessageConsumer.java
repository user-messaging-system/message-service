package com.user_messaging_system.message_service.rabbitmq.consumer;

import com.user_messaging_system.message_service.api.input.MessageSendInput;
import com.user_messaging_system.message_service.websocket.session.WebSocketSessionManager;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Objects;

@Component
public class WebSocketMessageConsumer {
    @RabbitListener(queues = "message-ws-send-queue")
    public void receiveMessage(MessageSendInput messageSendInput){
        String receiverId = messageSendInput.getReceiverId();

        if(checkReceiverIsOffline(receiverId)){
            System.out.println("Receiver is offline");
            return;
        }

        WebSocketSession session = WebSocketSessionManager.getSession(receiverId);

        if(checkSessionIsClose(session)){
            System.out.println("Session is closed");
            return;
        }

        try{
            session.sendMessage(convertToWebSocketMessage(messageSendInput.getContent()));
        } catch (IOException e) {
            System.out.println("Error while sending message");
        }
    }

    private boolean checkSessionIsClose(WebSocketSession session){
        return Objects.isNull(session) || !session.isOpen();
    }

    private boolean checkReceiverIsOffline(String receiverId){
        return Objects.isNull(WebSocketSessionManager.getSession(receiverId));
    }

    private WebSocketMessage<String> convertToWebSocketMessage(String message){
        return new TextMessage(message);
    }
}
