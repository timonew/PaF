package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.config.AuthStatus;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.config.RegistrationStatus;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.GameStartDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.LoginRequestDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.PlayerDetailsDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.RegisterRequestDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.SimpleGameDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.SpielDetailsDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.UpdateGameStatusDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.WaitingGameDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spieler;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.filter.JwtUtil;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.service.GameService;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.service.UserService;




@RestController
@RequestMapping("/rest")
public class RestApiController {
	
    @Autowired
    private WebSocketController webSocketController;
    
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final GameService gameService;

    // Konstruktor-Injektion
    public RestApiController(UserService userService, JwtUtil jwtUtil, GameService gameService) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.gameService = gameService;
    }

    @PostMapping("/user/register")
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

    @PostMapping("/user/login")
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


    @GetMapping("/user/details")
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
    
    @PostMapping("/game/start")
    public ResponseEntity<WaitingGameDTO> startGame(@RequestBody GameStartDTO gameStartDTO, Authentication authentication) {
    	System.out.println("Controller `/game/start` aufgerufen");
        // Ausgabe der empfangenen Daten in der Konsole
        System.out.println("Eingehende Daten: " + gameStartDTO);
        
        // Falls Authentication benötigt wird, hier die Prüfung lassen:
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("Unauthenticated Zugriff erkannt.");
            return ResponseEntity.status(401).build();
        }

        String username = authentication.getName();
        WaitingGameDTO dto = gameService.startGame(gameStartDTO, username);
        return ResponseEntity.ok(dto);
  

    }
    
    @PostMapping("/game/updateStatus")
    public ResponseEntity<String> updateGameStatus(@RequestBody UpdateGameStatusDTO updateGameStatusDTO) {
        if (updateGameStatusDTO == null || updateGameStatusDTO.getGameId() == null || updateGameStatusDTO.getNewStatus() == null) {
            throw new IllegalArgumentException("UpdateGameStatusDTO, GameId oder NewStatus darf nicht null sein.");
        }

        String status = gameService.setGameStatus(updateGameStatusDTO);
        return ResponseEntity.ok("Spielstatus wurde erfolgreich aktualisiert: " + status);
    }

    @GetMapping("/game/waiting")
    public ResponseEntity<List<SimpleGameDTO>> getWaitingGames(Authentication authentication) {
        System.out.println("Controller `/game/waiting` aufgerufen");

        // Falls Authentication benötigt wird, hier die Prüfung
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("Unauthenticated Zugriff erkannt.");
            return ResponseEntity.status(401).build();
        }

        // Den Benutzernamen der authentifizierten Person abrufen
        String username = authentication.getName();
        System.out.println("Authentifizierter Benutzer: " + username);

        // Wartende Spiele abrufen
        List<SimpleGameDTO> waitingGames = gameService.getWaitingGames();
        webSocketController.broadcastWaitingGames();

        System.out.println("Anzahl der wartenden Spiele: " + waitingGames.size());
        return ResponseEntity.ok(waitingGames);
    }

    
    

    /**
     * Tritt einem bestehenden Spiel bei.
     */
    @PostMapping("/game/join")
    public ResponseEntity<String> joinGame(@RequestBody Map<String, Long> requestBody, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        Long gameId = requestBody.get("gameId");
        if (gameId == null) {
            return ResponseEntity.status(400).body("Spiel-ID fehlt");
        }

        String username = authentication.getName();
        gameService.joinGame(gameId, username);

        return ResponseEntity.ok("Spiel erfolgreich beigetreten");
    }

        

    @GetMapping("/game/{spielId}")
    public ResponseEntity<Object> getSpielDetails(@PathVariable Long spielId, Authentication authentication) {
        // Überprüfung, ob der Benutzer authentifiziert ist
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        try {
            // Spielinformationen über den GameService abrufen
            SpielDetailsDTO spielDetails = gameService.getSpielDetails(spielId);
            
            // Die Spiel-Details in der Konsole ausgeben
            System.out.println("Spiel Details: " + spielDetails);  // Hier werden die Spielinformationen in der Konsole ausgegeben

            // Rückgabe der Spielinformationen als Response
            return ResponseEntity.ok(spielDetails);
        } catch (IllegalArgumentException e) {
            // Fehlerbehandlung: Spiel nicht gefunden
            return ResponseEntity.status(404).body(Map.of("error", "Spiel nicht gefunden"));
        }
    }



}
