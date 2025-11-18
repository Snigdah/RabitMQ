package com.example.notification_service.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.util.Map;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {

        String userId = null;

        // Extract userId from query parameter
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            userId = servletRequest.getServletRequest().getParameter("userId");
        }

        // Fallback: parse URI manually
        if (userId == null || userId.trim().isEmpty()) {
            userId = UriComponentsBuilder.fromUri(request.getURI())
                    .build()
                    .getQueryParams()
                    .getFirst("userId");
        }

        // Fallback: check headers
        if (userId == null || userId.trim().isEmpty()) {
            userId = request.getHeaders().getFirst("userId");
        }

        if (userId == null || userId.trim().isEmpty()) {
            System.err.println("❌ No userId found in request");
            userId = "anonymous-" + System.currentTimeMillis();
        }

        final String finalUserId = userId.trim();
        System.out.println("✅ WebSocket Principal created for user: " + finalUserId);

        return new Principal() {
            @Override
            public String getName() {
                return finalUserId;
            }

            @Override
            public String toString() {
                return finalUserId;
            }
        };
    }
}