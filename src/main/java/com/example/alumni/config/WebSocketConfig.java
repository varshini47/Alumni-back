// package com.example.alumni.config;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.messaging.simp.config.MessageBrokerRegistry;
// import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
// import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
// import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

// @Configuration
// @EnableWebSocketMessageBroker
// public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//     @Override
//     public void registerStompEndpoints(StompEndpointRegistry registry) {
//         registry.addEndpoint("/chat").setAllowedOrigins("*").withSockJS();
//     }

//     @Override
//     public void configureMessageBroker(MessageBrokerRegistry registry) {
//         registry.enableSimpleBroker("/queue"); // Enables private messaging
//         registry.setApplicationDestinationPrefixes("/app");
//         registry.setUserDestinationPrefix("/user");
//     }
// }


package com.example.alumni.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/user"); // Public (/topic) and private (/queue) messages
        config.setApplicationDestinationPrefixes("/app"); // Client sends messages with "/app"
        config.setUserDestinationPrefix("/user"); // Prefix for private messages
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat","/chat/*") // WebSocket endpoint
                .setAllowedOrigins("http://localhost:3000") // Allow frontend requests
                .withSockJS(); // Enable SockJS fallback for browser compatibility
    }
}
