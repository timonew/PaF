package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.controller;

import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spieler;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.service.UserService;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.filter.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller für die Benutzerverwaltung.
 * Bietet REST-Endpoints für die Registrierung und Anmeldung von Benutzern.
 * Nach erfolgreicher Anmeldung wird ein JWT-Token zurückgegeben, um die Authentifizierung zu verwalten.
 */
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Authentifiziert einen Benutzer basierend auf Benutzernamen und Passwort.
     * Wenn die Zugangsdaten korrekt sind, wird ein JWT-Token generiert und zurückgegeben.
     *
     * @param loginRequest Das Spieler-Objekt, das die Anmeldeinformationen enthält (Benutzername und Passwort).
     * @return Eine ResponseEntity mit dem generierten JWT-Token oder einer Fehlermeldung bei ungültigen Zugangsdaten.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Spieler loginRequest) {
        Spieler spieler = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());

        if (spieler != null) {
            // Generiert JWT-Token für den authentifizierten Benutzer
            String token = jwtUtil.generateToken(spieler.getUsername());
            return ResponseEntity.ok(token); // Gibt das Token als Antwort zurück
        } else {
            // Gibt HTTP 401 zurück bei ungültigen Zugangsdaten
            return ResponseEntity.status(401).body("Ungültige Zugangsdaten");
        }
    }

    /**
     * Registriert einen neuen Benutzer im System.
     *
     * @param spieler Das Spieler-Objekt, das die Registrierungsinformationen enthält.
     * @return Eine ResponseEntity mit einer Erfolgsmeldung bei erfolgreicher Registrierung.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Spieler spieler) {
        userService.registerUser(spieler);  // Registriert neuen Spieler über UserService
        return ResponseEntity.ok("Registrierung erfolgreich");
    }
}
