package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.config;

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
        // Einfacher Broker für Benachrichtigungen / Broadcast-Nachrichten
        config.enableSimpleBroker("/topic", "/queue"); // Hiermit können wir `/topic` und `/queue` verwenden
        config.setApplicationDestinationPrefixes("/app"); // Präfix für eingehende Nachrichten
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpunkt für Benutzerverbindungen, z.B. WebSocket-Verbindung auf /websocket
        registry.addEndpoint("/websocket")
                .setAllowedOrigins("http://localhost:3000") // Die originierende Adresse des Frontends
                .withSockJS(); // Falls WebSocket nicht unterstützt wird, SockJS als Fallback verwenden
    }
}

