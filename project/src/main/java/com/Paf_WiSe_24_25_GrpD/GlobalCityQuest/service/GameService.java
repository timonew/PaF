/**
 * Controller zur Bereistellung der Websockets.
 * 
 * @author Timo Neuwerk
 * @date 01.02.2025
 */
package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.service;

import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.controller.WebSocketController;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.*;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.MapLayer;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spiel;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spieler;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spielzug;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Stadt;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository.MapLayerRepository;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository.SpielRepository;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository.SpielerRepository;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository.SpielzugRepository;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository.StadtRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class GameService {
	
	@Autowired
	 private MapLayerRepository maplayerRepository;
	
	 @Autowired
	 private StadtRepository stadtRepository;

    @Autowired
    private SpielRepository spielRepository;

    @Autowired
    private SpielerRepository spielerRepository;
    
    @Autowired
    private SpielzugRepository spielzugRepository;

    @Autowired
    private WebSocketController webSocketController; 

    
    /**
     * Holt den Status eines Spiels.
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

         // Aktualisierte Liste der wartenden Spiele broadcasten
        getWaitingGames();
    }

    /**
     * Holt den Status eines Spiels.
     */
    public String getGameStatus(Long gameId) {
        Optional<Spiel> optionalSpiel = spielRepository.findById(gameId);
        return optionalSpiel.map(Spiel::getStatus)
                .orElseThrow(() -> new IllegalArgumentException("Spiel nicht gefunden: " + gameId));
    }

    /**
     * Aktualisiert den Status eines Spiels. z.B. wenn ein vorbereitetes Spiel entfernt wird 
     */
    public void setGameStatus(UpdateGameStatusDTO updateGameStatusDTO) {
        if (updateGameStatusDTO == null || updateGameStatusDTO.getGameId() == null || updateGameStatusDTO.getNewStatus() == null) {
            throw new IllegalArgumentException("UpdateGameStatusDTO, GameId oder NewStatus darf nicht null sein.");
        }

        Long gameId = updateGameStatusDTO.getGameId();
        String newStatus = updateGameStatusDTO.getNewStatus();

        Spiel spiel = spielRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Spiel nicht gefunden: " + gameId));
        
        spiel.setStatus(newStatus);
        spielRepository.save(spiel);

        // Aktualisierte Liste der wartenden Spiele broadcasten
        getWaitingGames();
    }

    /**
     * Bereitet ein Spiel für die Liste der wartenden Spiele vor 
     */
    public SimpleGameDTO startGame(GameStartDTO gameStartDTO, String username) {
        Spieler spieler1 = spielerRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Spieler nicht gefunden: " + username));

        Spiel neuesSpiel = new Spiel();
        neuesSpiel.setContinent(gameStartDTO.getContinent());
        neuesSpiel.setDifficultyLevel(gameStartDTO.getDifficulty());
        neuesSpiel.setSpieler1(spieler1);
        neuesSpiel.setStatus("waiting");

        
        spielRepository.save(neuesSpiel);


        // Aktualisierte Liste der wartenden Spiele broadcasten
        getWaitingGames();
        SimpleGameDTO dto = new SimpleGameDTO();
        dto.setId(neuesSpiel.getId());
        dto.setSpieler1Name(neuesSpiel.getSpieler1().getUserName());
        dto.setSpieler1ID(neuesSpiel.getSpieler1().getId());
        dto.setContinent(neuesSpiel.getContinent());
        dto.setDifficultyLevel(neuesSpiel.getDifficultyLevel());
        return dto;

    }

    /**
     * holt die Spiele mit Status "waiting" aus dem Repository 
     */
    public List<SimpleGameDTO> getWaitingGames() {
        List<Spiel> waitingGames = spielRepository.findByStatus("waiting");
        List<SimpleGameDTO> waitingGamesDTO = waitingGames.stream()
                .map(game -> {
                    SimpleGameDTO dto = new SimpleGameDTO();
                    dto.setId(game.getId());
                    dto.setSpieler1Name(game.getSpieler1().getUserName());
                    dto.setSpieler1ID(game.getSpieler1().getId());
                    dto.setContinent(game.getContinent());
                    dto.setDifficultyLevel(game.getDifficultyLevel());
                    return dto;
                })
                .collect(Collectors.toList());

        // Broadcast der aktualisierten Spieleliste an verbundene WebSocket-Clients
        webSocketController.sendWaitingGames(waitingGamesDTO);

        return waitingGamesDTO;
    }

    
    /**
     * Spieler 2 sendet eine Anfrage an Spieler 1 anhand eine vorbereiteten Spiels
     */
    public GameRequestDTO sendGameRequest(Long gameId, String requestingPlayer) {
        // Spiel und Spieler laden
        Spiel spiel = spielRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Spiel nicht gefunden: " + gameId));

        Spieler spieler2 = spielerRepository.findByUserName(requestingPlayer)
                .orElseThrow(() -> new UsernameNotFoundException("Spieler nicht gefunden: " + requestingPlayer));

        // Sicherstellen, dass das Spiel noch verfügbar ist
        if (!spiel.getStatus().equals("waiting")) {
            throw new IllegalStateException("Spiel ist nicht verfügbar: " + gameId);
        }

        // GameRequestDTO erstellen
        GameRequestDTO requestDTO = new GameRequestDTO(requestingPlayer, gameId);
        requestDTO.setGameId(spiel.getId());
        requestDTO.setRequestingPlayer(spieler2.getUserName());

        return requestDTO;
    }

    /**
     * Spielanfrage wird akzeptiert; Status wird geändert und die ID von Spieler2 eingetragen
     */
    @Transactional
    public void acceptGameRequest(Long gameId, String player2Name) {
        // 1. Spiel finden
        Spiel spiel = spielRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Spiel nicht gefunden"));

        // 2. Spieler 2 finden
        Spieler spieler2 = spielerRepository.findByUserName(player2Name)
                .orElseThrow(() -> new IllegalArgumentException("Spieler 2 nicht gefunden"));

        // 3. Status auf IN_PROCESS setzen und Spieler 2 zuweisen
        spiel.setStatus("IN_PROCESS");
        spiel.setSpieler2(spieler2);

        // Änderungen speichern
        spielRepository.save(spiel);
    }

    /**
     * das Spiel wird initiiert
     */
    @Transactional
    public void gameInit(Long gameId) {
        System.out.println("gameInit called with gameId: " + gameId);

        // 1. Spiel finden
        Spiel spiel = spielRepository.findById(gameId)
                .orElseThrow(() -> {
                    System.out.println("Spiel mit gameId " + gameId + " nicht gefunden.");
                    return new IllegalArgumentException("Spiel nicht gefunden");
                });

        System.out.println("Spiel gefunden: " + spiel);

        // 2. Überprüfen, ob Spielzüge existieren, wenn nicht, dann erstellen
        List<Spielzug> spielzüge = spielzugRepository.findBySpielId(gameId);
        System.out.println("Gefundene Spielzüge für gameId " + gameId + ": " + spielzüge.size());

        if (spielzüge.isEmpty()) {
            System.out.println("Keine Spielzüge gefunden, neue werden erstellt.");
            List<Stadt> verfügbareStädte = stadtRepository.findByContinentAndDifficultyLevel(
                    spiel.getContinent(), spiel.getDifficultyLevel());

            System.out.println("Verfügbare Städte für Kontinent " + spiel.getContinent() +
                    " und Schwierigkeitsgrad " + spiel.getDifficultyLevel() + ": " + verfügbareStädte.size());

            Random random = new Random();
            List<Stadt> ausgewählteStädte = random.ints(0, verfügbareStädte.size())
                    .distinct()
                    .limit(10)
                    .mapToObj(verfügbareStädte::get)
                    .collect(Collectors.toList());

            System.out.println("Ausgewählte Städte: " + ausgewählteStädte);

            spielzüge = ausgewählteStädte.stream()
                    .map(stadt -> {
                        Spielzug spielzug = new Spielzug();
                        spielzug.setSpiel(spiel);
                        spielzug.setStadt(stadt);
                        return spielzug;
                    })
                    .collect(Collectors.toList());

            // Spielzüge speichern
            spielzugRepository.saveAll(spielzüge);
            System.out.println("Neue Spielzüge gespeichert: " + spielzüge.size());
        }

    }

    /**
     * das DTO zur Spielinitiierung wird erzeugt
     */
    public GameInitDTO getgameInitDTO(Long gameId) {
        System.out.println("called getGameInitDTO: " + gameId); // Log the incoming request

        // Hole das Spiel aus der Datenbank
        Spiel spiel = spielRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Spiel nicht gefunden: " + gameId));
        System.out.println("Spiel gefunden: " + spiel);

        // Hole die Spielzüge für das Spiel
        List<Spielzug> spielzüge = spielzugRepository.findBySpielId(gameId);
        System.out.println("Spielzüge gefunden: " + spielzüge.size());
        
        Spieler spieler1 = spiel.getSpieler1();
        Spieler spieler2 = spiel.getSpieler2();
        
        MapLayer mapLayer = maplayerRepository.findByMapContinent(spiel.getContinent());
        

        // 4. DTO erstellen
        GameInitDTO dto = new GameInitDTO();
        dto.setSpieler1Id(spieler1.getId());
        dto.setSpieler2Id(spieler2.getId());
        dto.setSpieler1Name(spieler1.getUserName());
        dto.setSpieler2Name(spieler2.getUserName());
        dto.setGameId(spiel.getId());
        dto.setStatus(spiel.getStatus());
        dto.setContinent(spiel.getContinent());
        dto.setDifficultyLevel(spiel.getDifficultyLevel());
        dto.setMapCoordinates(mapLayer.getMapCoordinates());
        dto.setZoomLevel(mapLayer.getMapZoom());

        
        if (spielzüge.isEmpty()) {
            System.out.println("Keine Spielzüge für das Spiel mit ID " + gameId);
        }

        dto.setSpielzuege(spielzüge.stream().map(spielzug -> {
            SpielzugDTO spielzugDTO = new SpielzugDTO();
            spielzugDTO.setSpielZugId(spielzug.getId());
            
            // Stadt aus dem Spielzug abrufen
            Stadt stadt = spielzug.getStadt();
            if (stadt != null) {
                // ID, Name und Koordinaten der Stadt setzen
                spielzugDTO.setStadtId(stadt.getId());
                spielzugDTO.setStadtName(stadt.getStadtName());
                spielzugDTO.setKoordinaten(stadt.getKoordinaten());
            } else {
                System.err.println("Warnung: Kein Stadtobjekt für Spielzug mit ID " + spielzug.getId());
            }

            return spielzugDTO;
        }).collect(Collectors.toList()));


        return dto;
    }
    
    
    /**
     * ein eingehender Spielzug wird verarbeitet
     */
    @Transactional
    public void processGuess(Long gameId, Long spielZugId, Long spielZugScore, Long spielerId, boolean spieler1Bool, String spielZugGuessStr) {
        // Spielzug validieren
        Optional<Spielzug> moveOpt = spielzugRepository.findById(spielZugId);
        if (moveOpt.isEmpty()) {
            throw new IllegalArgumentException("Spielzug nicht gefunden mit ID: " + spielZugId);
        }

        Spielzug move = moveOpt.get();

        // Score und Guess für den aktuellen Spielzug setzen
        if (spieler1Bool) {
            move.setScoreSpieler1(spielZugScore);
            move.setGuessSpieler1(spielZugGuessStr);
        } else {
            move.setScoreSpieler2(spielZugScore);
            move.setGuessSpieler2(spielZugGuessStr);
        }

        // Änderungen im Repository speichern
        spielzugRepository.save(move);

        // Liste aller Spielzüge für das aktuelle Spiel abrufen
        List<Spielzug> moves = spielzugRepository.findBySpielId(gameId);

 
        // DTO für den WebSocket-Broadcast erstellen
        GuessBroadcastDTO broadcastDTO = new GuessBroadcastDTO();
        broadcastDTO.setSpielZugId(move.getId());
        if (spieler1Bool) {
            broadcastDTO.setScoreSpieler1(move.getScoreSpieler1());
            broadcastDTO.setGuessSpieler1(move.getGuessSpieler1());
        } else {
            broadcastDTO.setScoreSpieler2(move.getScoreSpieler2());
            broadcastDTO.setGuessSpieler2(move.getGuessSpieler2());
        }


        // Broadcast durchführen
        webSocketController.broadcastGuess(gameId, broadcastDTO);
       
    }


}
