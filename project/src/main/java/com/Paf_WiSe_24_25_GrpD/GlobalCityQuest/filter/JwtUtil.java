package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component

public class JwtUtil {

    private static final String SECRET_KEY = "gRzTNzNtedu+8f5Lpw44y4xRBlZpDR0nW3soChMtLQs="; // Verwende einen sicheren Schlüssel

    // Methode zur Token-Erstellung
    public String generateToken(String username) {
        // Erstelle ein SecretKey-Objekt aus dem SECRET_KEY
        Key signingKey = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());

        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 Stunden Gültigkeit
                .signWith(signingKey);  // Verwende den SecretKey zum Signieren des Tokens

        return jwtBuilder.compact();
    }

    // Methode zur Token-Validierung
    public Boolean validateToken(String token, String username) {
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }

    // Methode zum Extrahieren des Benutzernamens aus dem Token
    public static String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Prüfen, ob Token abgelaufen ist
    private Boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // Hilfsmethode zum Extrahieren der Claims
    private static Claims extractClaims(String token) {
        Key signingKey = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());

        return Jwts.parserBuilder() // Verwende den neuen ParserBuilder
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
