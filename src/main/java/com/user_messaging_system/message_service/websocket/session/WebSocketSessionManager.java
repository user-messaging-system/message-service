package com.user_messaging_system.message_service.websocket.session;

import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketSessionManager {
    private static ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public static void addSession(String userId, WebSocketSession session) {
        sessions.put(userId, session);
    }

    public static WebSocketSession getSession(String userId) {
        return sessions.get(userId);
    }

    public static void removeSession(String userId) {
        try(WebSocketSession session = sessions.remove(userId)){

        }catch(IOException e) {
            System.out.println("Error while closing session" + e.getMessage());
        }
    }
}