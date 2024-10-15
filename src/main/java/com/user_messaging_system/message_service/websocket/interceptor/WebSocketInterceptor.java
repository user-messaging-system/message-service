package com.user_messaging_system.message_service.websocket.interceptor;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class WebSocketInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes
    ) throws IOException {
        String userId = exctractUserId(request);

        if(isInvalidUserId(userId)){
            response.setStatusCode(HttpStatus.BAD_REQUEST);
            return false;
        }

        if(isNotExist(userId)){
            response.setStatusCode(HttpStatus.NOT_FOUND);
            return false;
        }

        attributes.put("userId", userId);
        return true;
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception
    ) {
        System.out.println("Hello World");
    }

    private String exctractUserId(ServerHttpRequest request){
        return UriComponentsBuilder.fromUri(request.getURI())
                .build()
                .getQueryParams().getFirst("userId");
    }

    private boolean isInvalidUserId(String userId){
        return Objects.isNull(userId) || userId.isEmpty();
    }

    private boolean isNotExist(String userId){
        return false;
    }
}
