package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.controller;

import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.GameMoveDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.GameStartDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.PlayerDetailsDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.WaitingGameDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spiel;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.service.GameService;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller für die Spiellogik.
 * Bietet REST-Endpunkte zur Spielinitialisierung, für Spieleraktionen und zur Verwaltung des Spielverlaufs.
 */
@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/start")
    public ResponseEntity<WaitingGameDTO> startGame(@RequestBody GameStartDTO gameStartDTO, @RequestParam String username) {
        WaitingGameDTO dto = gameService.startGame(gameStartDTO, username);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/waiting")
    public ResponseEntity<List<WaitingGameDTO>> getWaitingGames() {
        return ResponseEntity.ok(gameService.getWaitingGames());
    }

    @PostMapping("/join")
    public ResponseEntity<String> joinGame(@RequestParam Long gameId, @RequestParam String username) {
        gameService.joinGame(gameId, username);
        return ResponseEntity.ok("Spiel erfolgreich beigetreten");
    }

    @PostMapping("/end")
    public ResponseEntity<String> endGame(@RequestParam Long gameId) {
        gameService.endGame(gameId);
        return ResponseEntity.ok("Spiel erfolgreich beendet");
    }

    @PostMapping("/move")
    public ResponseEntity<String> makeMove(@RequestBody GameMoveDTO move) {
        gameService.makeMove(move);
        return ResponseEntity.ok("Spielzug erfolgreich");
    }

    @GetMapping("/status")
    public ResponseEntity<String> getGameStatus(@RequestParam Long gameId) {
        return ResponseEntity.ok(gameService.getGameStatus(gameId));
    }
}
