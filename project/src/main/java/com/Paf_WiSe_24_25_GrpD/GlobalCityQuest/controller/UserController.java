package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.controller;

import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.config.AuthStatus;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.config.RegistrationStatus;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spieler;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.service.UserService;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.filter.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Registrierung eines neuen Benutzers.
     *
     * @param spieler Die Daten des Benutzers.
     * @return Der Status der Registrierung.
     */
    @PostMapping("/register")
    public ResponseEntity<RegistrationStatus> registerUser(@RequestBody Spieler spieler) {
        // Aufruf der UserService-Methoden zur Registrierung und Rückgabe des Status
        RegistrationStatus status = userService.registerUser(spieler);
        
        // Rückgabe der Antwort mit dem entsprechenden Status
        if (status == RegistrationStatus.SUCCESS) {
            return ResponseEntity.ok(status); // Benutzer erfolgreich registriert
        } else {
            return ResponseEntity.status(400).body(status); // Fehlerhafte Registrierung
        }
    }

    /**
     * Login eines Benutzers.
     *
     * @param loginRequest Die Login-Daten des Benutzers.
     * @return JWT-Token bei Erfolg oder Authentifizierungsstatus bei Fehler.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Spieler loginRequest) {
        // Authentifizierung über den UserService
        AuthStatus authStatus = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());

        if (authStatus == AuthStatus.SUCCESS) {
            // Wenn die Authentifizierung erfolgreich ist, JWT-Token generieren
            String token = jwtUtil.generateToken(loginRequest.getUsername());
            return ResponseEntity.ok(token); // Token wird zurückgegeben
        } else {
            // Falls die Authentifizierung fehlschlägt, den entsprechenden Status zurückgeben
            return ResponseEntity.status(401).body(authStatus);
        }
    }
}
