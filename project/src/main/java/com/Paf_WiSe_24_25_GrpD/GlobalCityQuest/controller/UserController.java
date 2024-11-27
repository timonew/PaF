package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.config.AuthStatus;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.config.RegistrationStatus;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.LoginRequestDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.PlayerDetailsDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.RegisterRequestDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spieler;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.filter.JwtUtil;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    // Konstruktor-Injektion
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationStatus> registerUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
        try {
            // Spieler-Objekt aus DTO erstellen
            Spieler spieler = new Spieler();
            spieler.setName(registerRequestDTO.getName());
            spieler.setUserName(registerRequestDTO.getUserName());
            spieler.setPassword(registerRequestDTO.getPassword());
            spieler.setCurrentscore(0); // Initialisieren des Scores

            // Registrierung des Benutzers
            RegistrationStatus status = userService.registerUser(spieler);

            if (status == RegistrationStatus.SUCCESS) {
                return ResponseEntity.ok(status);
            } else {
                return ResponseEntity.status(400).body(status);
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(RegistrationStatus.ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        // Logge den eingehenden Request
        System.out.println("Login-Anfrage für Benutzer: " + loginRequestDTO.getUserName());
        
        AuthStatus authStatus = userService.authenticate(
            loginRequestDTO.getUserName(),
            loginRequestDTO.getPassword()
        );
        
        // Logge den Authentifizierungsstatus
        System.out.println("Authentifizierungsstatus: " + authStatus);
        
        if (authStatus == AuthStatus.SUCCESS) {
            String token = jwtUtil.generateToken(loginRequestDTO.getUserName());
            System.out.println("Token erfolgreich generiert für Benutzer: " + loginRequestDTO.getUserName());
            return ResponseEntity.ok(token);
        } else {
            System.out.println("Login fehlgeschlagen für Benutzer: " + loginRequestDTO.getUserName() + " mit Status: " + authStatus);
            return ResponseEntity.status(401).body(authStatus);
        }
    }


    @GetMapping("/details")
    public ResponseEntity<?> getUserDetails(Authentication authentication) {
    	System.out.println("Controller `/user/details` aufgerufen");

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        try {
            // Benutzername aus dem Security-Kontext holen (dieser wird nach der JWT-Validierung gesetzt)
            String username = authentication.getName();  // Benutzername des authentifizierten Benutzers

            // PlayerDetailsDTO anhand des Benutzernamens abrufen
            PlayerDetailsDTO playerDetails = userService.getPlayerDetailsByUsername(username);
            return ResponseEntity.ok(playerDetails);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving user details");
        }
    }
}
