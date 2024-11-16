package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private static String SECRET_KEY = "your_secret_key"; // Verwende einen sicheren Schlüssel

    // Methode zur Token-Erstellung
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 Stunden Gültigkeit
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
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
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

	public static boolean validateToken(String token) {
		// TODO Auto-generated method stub
		return false;
	}
}
