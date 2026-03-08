package com.backend.ecommercespringbootbackend.webSocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/messages");  // Where clients subscribe
        config.setApplicationDestinationPrefixes("/backend"); // Where clients send message
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/api/websocket") //endpoint for angular
                .setAllowedOriginPatterns("*")
                .withSockJS(); // enables SockJS fallback
    }
}