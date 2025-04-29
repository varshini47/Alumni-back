package com.example.alumni.config;

// import org.springframework.context.event.EventListener;
// import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
// import org.springframework.stereotype.Component;
// import org.springframework.web.socket.messaging.SessionConnectedEvent;
// import org.springframework.web.socket.messaging.SessionDisconnectEvent;

// @Component
// public class WebSocketEventListener {

//     @EventListener
//     public void handleWebSocketConnectListener(SessionConnectedEvent event) {
//         StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//         System.out.println("New connection: " + headerAccessor.getUser());
//     }

//     @EventListener
//     public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
//         StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//         System.out.println("User disconnected: " + headerAccessor.getUser());
//     }
// }

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.messaging.Message;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketEventListener {
    
    // ✅ Store userId and sessionId mapping
    private static final Map<String, String> sessionUserMap = new ConcurrentHashMap<>();

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        Object rawMessage = headerAccessor.getHeader("simpConnectMessage");
        if (rawMessage instanceof Message<?>) {
            Message<?> connectMessage = (Message<?>) rawMessage;
            StompHeaderAccessor connectHeaderAccessor = StompHeaderAccessor.wrap(connectMessage);

            // ✅ Extract userId from CONNECT headers
            String userId = connectHeaderAccessor.getFirstNativeHeader("userId");
            String sessionId = connectHeaderAccessor.getSessionId();

            if (userId != null) {
                sessionUserMap.put(sessionId, userId);
                System.out.println("✅ User connected: ID = " + userId + ", Session = " + sessionId);
            } else {
                System.out.println("⚠️ User ID is null during WebSocket connection.");
            }
        } else {
            System.out.println("⚠️ simpConnectMessage not found or incorrect type.");
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        String userId = sessionUserMap.remove(sessionId); // Remove user from map

        if (userId != null) {
            System.out.println("❌ User disconnected: ID = " + userId + ", Session = " + sessionId);
        } else {
            System.out.println("⚠️ Unknown user disconnected, Session = " + sessionId);
        }
    }
}
