package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.GameMoveDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.GameStartDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.service.GameService;

/**
 * Controller für die Spiellogik.
 * Bietet REST-Endpunkte zur Spielinitialisierung, für Spieleraktionen und zur Verwaltung des Spielverlaufs.
 */
@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    private GameService gameService;

    /**
     * Initialisiert ein neues Spiel und konfiguriert es mit Schwierigkeitsgrad und Kontinent.
     *
     * @param gameStartDTO Ein DTO, das Schwierigkeitsgrad und Kontinent für das neue Spiel enthält.
     * @return Eine ResponseEntity mit einer Erfolgsmeldung und den Spielinformationen.
     */
    @PostMapping("/start")
    public ResponseEntity<?> startGame(@RequestBody GameStartDTO gameStartDTO) {
        return ResponseEntity.ok(gameService.startGame(gameStartDTO));
    }

    /**
     * Ermöglicht einem zweiten Spieler, einem bereits vorbereiteten Spiel beizutreten.
     *
     * @param gameId Die ID des vorbereiteten Spiels, dem beigetreten werden soll.
     * @return Eine ResponseEntity mit Bestätigung, dass der zweite Spieler dem Spiel beigetreten ist.
     */
    @PostMapping("/join")
    public ResponseEntity<?> joinGame(@RequestParam Long gameId) {
        return ResponseEntity.ok(gameService.joinGame(gameId));
    }

    /**
     * Nimmt einen Spielzug entgegen, der durch den Client generiert wurde. 
     * Der Spielzug beinhaltet die Koordinaten des Schätzens und wird ausgewertet.
     *
     * @param move Ein GameMoveDTO, das die Koordinaten des Zuges sowie Spielerinformationen enthält.
     * @return Eine ResponseEntity mit den Ergebnissen des Zuges, z. B. wie weit der Guess von der Zielstadt entfernt ist.
     */
    @PostMapping("/move")
    public ResponseEntity<?> makeMove(@RequestBody GameMoveDTO move) {
        return ResponseEntity.ok(gameService.makeMove(move));
    }

    /**
     * Gibt den aktuellen Spielstand zurück, einschließlich Informationen über den Fortschritt, die Spieler und den Punktestand.
     *
     * @param gameId Die ID des Spiels, für das der Status abgerufen werden soll.
     * @return Eine ResponseEntity mit den Spielstatusinformationen.
     */
    @GetMapping("/status")
    public ResponseEntity<?> getGameStatus(@RequestParam Long gameId) {
        return ResponseEntity.ok(gameService.getGameStatus(gameId));
    }

    /**
     * Beendet ein laufendes Spiel und speichert ggf. die Ergebnisse, um den Endstand festzuhalten.
     *
     * @param gameId Die ID des Spiels, das beendet werden soll.
     * @return Eine ResponseEntity mit einer Bestätigung der Spielbeendigung.
     */
    @PostMapping("/end")
    public ResponseEntity<?> endGame(@RequestParam Long gameId) {
        return ResponseEntity.ok(gameService.endGame(gameId));
    }
}
