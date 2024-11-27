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

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);

            try {
                String username = JwtUtil.extractUsername(jwt);
                if (jwtUtil.validateToken(jwt, username)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>()); // Optionale Authorities hinzufügen
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    System.out.println("JWT validiert und Benutzer im Security-Kontext gesetzt: " + username);
                } else {
                    System.out.println("JWT ist ungültig oder abgelaufen");
                }
            } catch (Exception e) {
                System.out.println("Fehler bei der JWT-Validierung: " + e.getMessage());
            }
        }

        // Weiterleitung der Anfrage an die nächste Station in der Filterkette
        chain.doFilter(request, response);

        System.out.println("Anfrage wird an den Controller weitergeleitet");
    }


}
