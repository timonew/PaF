package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.config.AuthStatus;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.config.RegistrationStatus;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.GameAnswerDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.GameRequestDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.GameStartDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.GameInitDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.LoginRequestDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.PlayerDetailsDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.RegisterRequestDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.SimpleGameDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.SpielDetailsDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.UpdateGameStatusDTO;
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

    // Registrierung des Benutzers
    @PostMapping("/user/register")
    public ResponseEntity<RegistrationStatus> registerUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
        try {
            Spieler spieler = new Spieler();
            spieler.setName(registerRequestDTO.getName());
            spieler.setUserName(registerRequestDTO.getUserName());
            spieler.setPassword(registerRequestDTO.getPassword());
            spieler.setCurrentscore(0); // Initialisieren des Scores

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

    // Login-Endpoint
    @PostMapping("/user/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        System.out.println("Login-Anfrage für Benutzer: " + loginRequestDTO.getUserName());
        
        AuthStatus authStatus = userService.authenticate(
            loginRequestDTO.getUserName(),
            loginRequestDTO.getPassword()
        );
        
        if (authStatus == AuthStatus.SUCCESS) {
            String token = jwtUtil.generateToken(loginRequestDTO.getUserName());
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(401).body(authStatus);
        }
    }

    // Benutzer-Details
    @GetMapping("/user/details")
    public ResponseEntity<?> getUserDetails(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        try {
            String username = authentication.getName();
            PlayerDetailsDTO playerDetails = userService.getPlayerDetailsByUsername(username);
            return ResponseEntity.ok(playerDetails);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving user details");
        }
    }

    // Start eines Spiels
    @PostMapping("/game/start")
    public ResponseEntity<SimpleGameDTO> startGame(@RequestBody GameStartDTO gameStartDTO, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        String username = authentication.getName();
        SimpleGameDTO dto = gameService.startGame(gameStartDTO, username);

        // WebSocket: Sende die Spielstartnachricht an alle Spieler, die das Spiel beobachten

        
        return ResponseEntity.ok(dto);
    }
    // Spiele anzeigen, die auf Spieler warten
    @GetMapping("/game/waiting")
    public ResponseEntity<List<SimpleGameDTO>> getWaitingGames(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }


        List<SimpleGameDTO> waitingGames = gameService.getWaitingGames();
        // WebSocket: Sende die Liste der wartenden Spiele an den Client
        webSocketController.sendWaitingGames(waitingGames);

        return ResponseEntity.ok(waitingGames);
    }

    @PostMapping("/game/joinrequest")
    public ResponseEntity<String> joinRequest(@RequestBody Map<String, Long> requestBody, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        Long gameId = requestBody.get("gameId");
        Long player1Id = requestBody.get("player1Id");

        if (gameId == null || player1Id == null) {
            return ResponseEntity.status(400).body("Spiel-ID oder Spieler 1 ID fehlt");
        }

        String username = authentication.getName();

        // Hier kannst du den WebSocket-Controller anrufen, um die Nachricht zu senden
        GameRequestDTO gameRequest = new GameRequestDTO(username, gameId);  // Beispiel DTO anpassen
        webSocketController.sendGameRequestToUser(player1Id, gameRequest);

        return ResponseEntity.ok("Spielanfrage erfolgreich gesendet");
    }

    @PostMapping("/game/requestAnswer")
    public ResponseEntity<String> requestAnswer(@RequestBody Map<String, String> requestBody, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String gameIdStr = requestBody.get("gameId");
        String player2Name = requestBody.get("player2Name");
        String decision = requestBody.get("decision");
        String player1Name = authentication.getName();

        // Überprüfen, ob gameId konvertiert werden kann
        Long gameId;
        try {
            gameId = Long.parseLong(gameIdStr);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("Invalid gameId format");
        }

        boolean gameAccepted = false;
        Long player1Id = userService.getIdByUsername(player1Name);
        Long player2Id = userService.getIdByUsername(player2Name);

        // Überprüfen, ob die Entscheidung akzeptiert wurde
        if ("requestAccepted".equalsIgnoreCase(decision)) {
            try {
                gameAccepted = true;
                // Spielanfrage akzeptieren und Spieler zuweisen
                gameService.acceptGameRequest(gameId, player2Name);

                // DTO erstellen und über WebSocket senden
                GameAnswerDTO gameAnswerDTO = new GameAnswerDTO(gameAccepted, gameId);
                webSocketController.sendGameAnswerToUser(gameAnswerDTO, player1Id);
                webSocketController.sendGameAnswerToUser(gameAnswerDTO, player2Id);
                
                gameService.gameInit(gameId);
                return ResponseEntity.ok("Spiel erfolgreich gestartet.");
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(400).body("Fehler: " + e.getMessage());
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Serverfehler: " + e.getMessage());
            }
        } else {
            // DTO für abgelehnte Anfrage erstellen
            GameAnswerDTO gameAnswerDTO = new GameAnswerDTO(gameAccepted, gameId);
            webSocketController.sendGameAnswerToUser(gameAnswerDTO, player1Id);
            webSocketController.sendGameAnswerToUser(gameAnswerDTO, player2Id);

            return ResponseEntity.ok("Spielanfrage wurde abgelehnt");
        }
    }

    @GetMapping("/game/init")
    public ResponseEntity<GameInitDTO> getGameInit(@RequestParam Long gameId) {
    	System.out.println("GET /game/init called with gameId: " + gameId); // Log the incoming request
         try {
        	 GameInitDTO gameInitDTO = gameService.getgameInitDTO(gameId);
            System.out.println("Game details successfully fetched for gameId " + gameId + ": " + gameInitDTO); 
            return ResponseEntity.ok(gameInitDTO);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

}