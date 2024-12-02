package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.filter.JwtUtil;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;

@Component
public class AuthChannelInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    public AuthChannelInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // Extrahiere den JWT aus den STOMP-Headern
        String token = accessor.getFirstNativeHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                String username = JwtUtil.extractUsername(token);

                if (jwtUtil.validateToken(token, username)) {
                    // Benutzer authentifizieren
                    UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    System.out.println("JWT validiert und Benutzer im Security-Kontext gesetzt: " + username);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Ungültiges Token: " + e.getMessage());
            }
        }

        return message;
    }
}
