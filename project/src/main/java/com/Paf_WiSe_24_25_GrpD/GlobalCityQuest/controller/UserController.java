// src/main/java/com/deinprojekt/controller/AuthController.java
package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.filter.JwtUtil;

@RestController
@RequestMapping("/api")
public class UserController {

    // Dummy-Methode zur Benutzerregistrierung
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> user) {
        String username = user.get("username");
        String password = user.get("password");

        // Hier würde normalerweise die Benutzerregistrierungslogik implementiert,
        // z. B. Benutzer in der Datenbank speichern.

        Map<String, String> response = new HashMap<>();
        response.put("message", "Benutzer erfolgreich registriert");
        return ResponseEntity.ok(response);
    }

    // Dummy-Methode zur Benutzeranmeldung und JWT-Token-Erstellung
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> user) {
        String username = user.get("username");
        String password = user.get("password");

        // In einer echten Anwendung würdest du hier die Anmeldedaten validieren.
        if ("benutzer".equals(username) && "passwort".equals(password)) {
            String token;
			try {
				token = JwtUtil.generateToken(username);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body("Ungültige Anmeldedaten");
        }
    }

    // Geschützter Endpunkt als Beispiel
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/protected")
    public ResponseEntity<?> protectedEndpoint(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (JwtUtil.validateToken(token)) {
            String username = new String();
            Map<String, String> response = new HashMap<>();
            response.put("message", "Hallo " + username + ", du hast Zugriff auf diesen geschützten Endpunkt!");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(403).body("Zugriff verweigert");
        }
    }
}
