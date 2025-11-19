package com.example.notification_service.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.security.Principal;

public class StompHeaderInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String token = accessor.getFirstNativeHeader("Authorization")
                    .replace("Bearer ", "");

            //String userId = jwtService.validateAndExtractUserId(token);
            String userId = accessor.getFirstNativeHeader("userId");
            System.out.println("CONNECT userId = " + userId);
            System.out.println("Authorization header = " + token);

            if (userId != null) {
                accessor.setUser(new StompPrincipal(userId));
                System.out.println("Principal set: " + userId);
            }
        }

        return message;
    }

    private static class StompPrincipal implements Principal {
        private final String name;
        public StompPrincipal(String name) { this.name = name; }
        @Override public String getName() { return name; }
    }

}
