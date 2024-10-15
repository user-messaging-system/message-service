package com.user_messaging_system.message_service.websocket.handler;

import com.user_messaging_system.message_service.websocket.session.WebSocketSessionManager;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component(value = "messageWebSocketHandler")
public class WebSocketHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception{
        WebSocketSessionManager.addSession(getUserId(session), session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
        System.out.println("New message received: " + message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{
        WebSocketSessionManager.removeSession(getUserId(session));
    }

    private String getUserId(WebSocketSession session){
        return (String)session.getAttributes().get("userId");
    }
}
