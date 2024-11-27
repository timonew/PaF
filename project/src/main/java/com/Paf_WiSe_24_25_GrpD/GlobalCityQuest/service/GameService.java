package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.service;

import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.GameMoveDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.GameStartDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.GameUpdateDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.WaitingGameDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spiel;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spieler;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository.SpielRepository;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository.SpielerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GameService {

    @Autowired
    private SpielRepository spielRepository;

    @Autowired
    private SpielerRepository spielerRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Startet ein neues Spiel.
     * @param request Spielstart-Daten.
     * @param username Benutzername des Spielers, der das Spiel erstellt.
     * @return DTO des wartenden Spiels.
     */
    public WaitingGameDTO startGame(GameStartDTO request, String username) {
        Spieler spieler = spielerRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Spieler nicht gefunden: " + username));

        Spiel spiel = new Spiel();
        spiel.setSpieler1(spieler);
        spiel.setDifficulty(request.getDifficulty());
        spiel.setContinent(request.getContinent());
        spiel.setStatus("waiting");
        spielRepository.save(spiel);

        WaitingGameDTO dto = new WaitingGameDTO();
        dto.setPlayer1Name(spieler.getUserName());
        dto.setDifficulty(request.getDifficulty());
        dto.setContinent(request.getContinent());
        messagingTemplate.convertAndSend("/topic/waitingGames", getWaitingGames());

        return dto;
    }

    /**
     * Gibt alle wartenden Spiele zurück.
     * @return Liste der wartenden Spiele als DTO.
     */
    public List<WaitingGameDTO> getWaitingGames() {
        return spielRepository.findByStatus("waiting").stream().map(spiel -> {
            WaitingGameDTO dto = new WaitingGameDTO();
            dto.setPlayer1Name(spiel.getSpieler1().getUserName());
            dto.setDifficulty(spiel.getDifficulty());
            dto.setContinent(spiel.getContinent());
            dto.setGameId(spiel.getId());
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * Spieler tritt einem bestehenden Spiel bei.
     * @param gameId ID des Spiels.
     * @param username Benutzername des Spielers.
     */
    public void joinGame(Long gameId, String username) {
        Spiel spiel = spielRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Spiel nicht gefunden: " + gameId));

        Spieler spieler2 = spielerRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Spieler nicht gefunden: " + username));

        if (!spiel.getStatus().equals("waiting")) {
            throw new IllegalStateException("Spiel ist nicht verfügbar: " + gameId);
        }

        spiel.setSpieler2(spieler2);
        spiel.setStatus("in_process");
        spielRepository.save(spiel);

        GameUpdateDTO update = new GameUpdateDTO();
        update.setGameId(gameId);
        update.setStatus("in_process");
        update.setCurrentState("Spiel gestartet: " + spiel.getDifficulty());

        messagingTemplate.convertAndSend("/topic/game/" + gameId, update);
        messagingTemplate.convertAndSend("/topic/waitingGames", getWaitingGames());
    }

    /**
     * Beendet ein Spiel und benachrichtigt alle Abonnenten.
     * @param gameId ID des Spiels.
     */
    public void endGame(Long gameId) {
        Spiel spiel = spielRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Spiel nicht gefunden: " + gameId));

        spiel.setStatus("ended");
        spielRepository.save(spiel);

        GameUpdateDTO update = new GameUpdateDTO();
        update.setGameId(gameId);
        update.setStatus("ended");
        update.setCurrentState("Spiel beendet.");

        messagingTemplate.convertAndSend("/topic/game/" + gameId, update);
    }

    /**
     * Verarbeitet einen Spielzug.
     * @param move Details des Spielzugs.
     * @return Aktualisiertes Spiel als DTO.
     */
    public GameUpdateDTO makeMove(GameMoveDTO move) {
        Spiel spiel = spielRepository.findById(move.getGameId())
                .orElseThrow(() -> new IllegalArgumentException("Spiel nicht gefunden: " + move.getGameId()));

        // Spielzug-Logik (vereinfachtes Beispiel)
        String player = move.getPlayer();
        String moveDetails = move.getMoveDetails();
        String message = player + " hat den Zug gemacht: " + moveDetails;

        GameUpdateDTO update = new GameUpdateDTO();
        update.setGameId(move.getGameId());
        update.setStatus(spiel.getStatus());
        update.setCurrentState(message);

        messagingTemplate.convertAndSend("/topic/game/" + move.getGameId(), update);
        return update;
    }

    /**
     * Holt den Status eines Spiels.
     * @param gameId ID des Spiels.
     * @return Status des Spiels.
     */
    public String getGameStatus(Long gameId) {
        Optional<Spiel> optionalSpiel = spielRepository.findById(gameId);
        return optionalSpiel.map(Spiel::getStatus)
                .orElseThrow(() -> new IllegalArgumentException("Spiel nicht gefunden: " + gameId));
    }
}
