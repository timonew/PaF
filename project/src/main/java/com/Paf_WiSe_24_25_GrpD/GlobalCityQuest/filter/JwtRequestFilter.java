package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.filter;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        String requestPath = request.getRequestURI();

        // Debug-Ausgabe des angefragten Pfads
        System.out.println("Angefragter Pfad: " + requestPath);

        if (requestPath.startsWith("/websocket")) {
               
                chain.doFilter(request, response);
                return;
               
        }

        // Prüfung auf vorhandenen JWT-Header
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            System.out.println("Token: " + jwt);

            try {
                // Extrahiere den Benutzernamen aus dem JWT
                String username = jwtUtil.extractUsername(jwt);

                // Validierung des JWT
                if (jwtUtil.validateToken(jwt, username)) {
                    // Erstelle ein Authentication-Token und setze es im Security-Kontext
                    UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>()); // Authorities können hier hinzugefügt werden
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    System.out.println("JWT validiert und Benutzer im Security-Kontext gesetzt: " + username);
                } else {
                    System.out.println("JWT ist ungültig oder abgelaufen.");
                }
            } catch (Exception e) {
                System.out.println("Fehler bei der JWT-Validierung: " + e.getMessage());
            }
        }

        // Weiterleitung der Anfrage an die nächste Station in der Filterkette
        chain.doFilter(request, response);
    }
}
