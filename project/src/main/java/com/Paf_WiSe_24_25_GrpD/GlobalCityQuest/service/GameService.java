package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.service;


import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.GameStartDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.SimpleGameDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.SpielDetailsDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.UpdateGameStatusDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.WaitingGameDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spiel;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spieler;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository.MapLayerRepository;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository.SpielRepository;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository.SpielerRepository;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Getter
@Setter
@Service
public class GameService {

    @Autowired
    private SpielRepository spielRepository;

    @Autowired
    private SpielerRepository spielerRepository;
    
    @Autowired
    private MapLayerRepository mapLayerRepository;

    
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
    
    /**
     * Aktualisiert den Status eines Spiels.
     *
     * @param updateGameStatusDTO Daten zur Aktualisierung des Spielstatus.
     * @return Neuer Status des Spiels.
     * @throws IllegalArgumentException falls das Spiel nicht gefunden wird.
     */
    public String setGameStatus(UpdateGameStatusDTO updateGameStatusDTO) {
        if (updateGameStatusDTO == null || updateGameStatusDTO.getGameId() == null || updateGameStatusDTO.getNewStatus() == null) {
            throw new IllegalArgumentException("UpdateGameStatusDTO, GameId oder NewStatus darf nicht null sein.");
        }

        Long gameId = updateGameStatusDTO.getGameId(); // Spiel-ID aus DTO extrahieren
        String newStatus = updateGameStatusDTO.getNewStatus(); // Neuer Status aus DTO extrahieren

        // Spiel im Repository suchen
        Optional<Spiel> optionalSpiel = spielRepository.findById(gameId);

        // Wenn das Spiel existiert, Status aktualisieren und speichern
        Spiel spiel = optionalSpiel.orElseThrow(() -> new IllegalArgumentException("Spiel nicht gefunden: " + gameId));
        spiel.setStatus(newStatus);
        spielRepository.save(spiel); // Änderungen speichern

        return spiel.getStatus(); // Aktualisierten Status zurückgeben
    }


    public WaitingGameDTO startGame(GameStartDTO gameStartDTO, String username) {
        // Spieler anhand des Benutzernamens aus JWT finden
        Spieler spieler1 = spielerRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Spieler nicht gefunden: " + username));

        // Neues Spiel-Objekt erstellen
        Spiel neuesSpiel = new Spiel();
        neuesSpiel.setContinent(gameStartDTO.getContinent());
        neuesSpiel.setDifficultyLevel(gameStartDTO.getDifficulty());
        neuesSpiel.setSpieler1(spieler1);
        neuesSpiel.setStatus("waiting"); // Spielstatus initialisieren

        // Spiel in der Datenbank speichern
        spielRepository.save(neuesSpiel);


        // Daten für die Rückgabe vorbereiten
        WaitingGameDTO waitingGameDTO = new WaitingGameDTO();
        waitingGameDTO.setGameId(neuesSpiel.getId());
        waitingGameDTO.setContinent(neuesSpiel.getContinent());
        waitingGameDTO.setDifficultyLevel(neuesSpiel.getDifficultyLevel());
        waitingGameDTO.setStatus(neuesSpiel.getStatus());

        return waitingGameDTO;
    }

    public List<SimpleGameDTO> getWaitingGames() {
        List<Spiel> waitingGames = spielRepository.findByStatus("waiting");
        return waitingGames.stream()
                .map(game -> {
                    SimpleGameDTO dto = new SimpleGameDTO();
                    dto.setId(game.getId());
                    dto.setSpieler1Name(game.getSpieler1().getUserName());
                    dto.setContinent(game.getContinent());
                    dto.setDifficultyLevel(game.getDifficultyLevel());
                    return dto;
                })
                .collect(Collectors.toList());
        		
    }
    


 ;

    public SpielDetailsDTO getSpielDetails(Long spielId) {
        // Spielinformationen abrufen (dieser Teil bleibt wie vorher)
        Spiel spiel = spielRepository.findById(spielId)
                .orElseThrow(() -> new IllegalArgumentException("Spiel nicht gefunden"));

        // Erstelle DTO für Spielinformationen
        SpielDetailsDTO spielDetailsDTO = new SpielDetailsDTO();
        spielDetailsDTO.setSpielId(spiel.getId());
        spielDetailsDTO.setContinent(spiel.getContinent());
        spielDetailsDTO.setDifficultyLevel(spiel.getDifficultyLevel());
        spielDetailsDTO.setStatus(spiel.getStatus());
        spielDetailsDTO.setSpieler1Name(spiel.getSpieler1().getUserName());
        spielDetailsDTO.setSpieler2Name(spiel.getSpieler2() != null ? spiel.getSpieler2().getUserName() : null);

		/*
		 * // Karte aus MapLayer suchen (Kontinent und DifficultyLevel 0) MapLayer
		 * mapLayer =
		 * mapLayerRepository.findByMapContinentAndMapDifficultyLevel(spiel.getContinent
		 * (), 0); if (mapLayer != null) { // Lade das Bild als Byte-Array File mapFile
		 * = new File(mapLayerDirectory + "/" + mapLayer.getLayerPath()); try { byte[]
		 * mapImageBytes = Files.readAllBytes(mapFile.toPath()); // Füge die Bilddaten
		 * als Byte-Array ins DTO hinzu spielDetailsDTO.setMapImage(mapImageBytes); }
		 * catch (IOException e) { throw new
		 * RuntimeException("Fehler beim Laden der Karte", e); } }
		 */

        return spielDetailsDTO;
    }

}
