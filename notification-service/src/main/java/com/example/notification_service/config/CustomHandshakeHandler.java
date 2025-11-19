package com.example.notification_service.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {

        // Extract userId from query parameters
        String userId = extractUserIdFromQuery(request);

        if (userId != null) {
            System.out.println("✅ Handshake - User authenticated: " + userId);
            return new StompPrincipal(userId);
        }

        // Fallback for anonymous users
        String anonymousUser = "anonymous-" + System.currentTimeMillis();
        System.out.println("⚠️ Handshake - Anonymous user: " + anonymousUser);
        return new StompPrincipal(anonymousUser);
    }

    private String extractUserIdFromQuery(ServerHttpRequest request) {
        String query = request.getURI().getQuery();
        if (query != null) {
            for (String param : query.split("&")) {
                if (param.startsWith("userId=")) {
                    return param.substring(7);
                }
            }
        }
        return null;
    }

    @Getter
    @ToString
    @AllArgsConstructor
    public static class StompPrincipal implements Principal {
        private final String name;
    }
}