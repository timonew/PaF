/**
 * Dieser Controller verwaltet alle WebSocket-Kommunikationen zwischen dem Server und den Clients.
 * Er sendet Nachrichten zu wartenden Spielen, Spielanfragen, Antworten und Guess-Daten.
 * 
 * @author Timo Neuwerk
 * @date 01.02.2025
 */

package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.service;

import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.config.AuthStatus;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.config.RegistrationStatus;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.HighscoreDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.PlayerDetailsDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Highscore;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spiel;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spieler;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository.HighscoreRepository;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository.SpielRepository;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository.SpielerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service-Klasse zur Verwaltung der Benutzer und deren Authentifizierung,
 * Registrierung sowie zum Abrufen von Spieler-Details und Highscores.
 */
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private SpielRepository spielRepository;

    @Autowired
    private SpielerRepository spielerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HighscoreRepository highscoreRepository;

    /**
     * Registriert einen neuen Benutzer, verschlüsselt das Passwort und speichert ihn in der Datenbank.
     */
    public RegistrationStatus registerUser(Spieler spieler) {
        if (isUserExists(spieler.getUserName())) {
            return RegistrationStatus.USER_ALREADY_EXISTS;
        }

        if (!isValid(spieler)) {
            return RegistrationStatus.INVALID_DATA;
        }

        // Passwort verschlüsseln, bevor es gespeichert wird
        spieler.setPassword(passwordEncoder.encode(spieler.getPassword()));
        saveUser(spieler);
        return RegistrationStatus.SUCCESS;
    }

    /**
     * Speichert einen Benutzer in der Datenbank.
     */
    private void saveUser(Spieler spieler) {
        spielerRepository.save(spieler);
    }

    /**
     * Authentifiziert einen Benutzer basierend auf seinem Benutzernamen und Passwort.
     */
    public AuthStatus authenticate(String userName, String password) {
        Spieler spieler = findUserByUsername(userName);

        if (spieler == null) {
            System.out.println("Benutzername nicht gefunden: " + userName); // Logge den Fehler
            return AuthStatus.USER_NOT_FOUND;
        }

        // Passwort überprüfen
        if (!passwordEncoder.matches(password, spieler.getPassword())) {
            System.out.println("Ungültiges Passwort für Benutzer: " + userName); // Logge den Fehler
            return AuthStatus.INVALID_CREDENTIALS;
        }

        System.out.println("Authentifizierung erfolgreich für Benutzer: " + userName); // Logge den Erfolg
        return AuthStatus.SUCCESS;
    }

    /**
     * Lädt die Benutzerinformationen für den angegebenen Benutzernamen.
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<Spieler> spielerOpt = spielerRepository.findByUserName(userName);
        if (spielerOpt.isEmpty()) {
            throw new UsernameNotFoundException("Benutzer nicht gefunden: " + userName);
        }
        Spieler spieler = spielerOpt.get();
        return org.springframework.security.core.userdetails.User.builder()
                .username(spieler.getUserName())
                .password(spieler.getPassword())
                .roles("USER") // Hier können zusätzliche Rollen hinzugefügt werden
                .build();
    }

    /**
     * Überprüft, ob der Benutzername bereits existiert.
     */
    private boolean isUserExists(String userName) {
        return spielerRepository.findByUserName(userName).isPresent();
    }

    /**
     * Validiert die Benutzerinformationen.
     */
    private boolean isValid(Spieler spieler) {
        // Validierung der Benutzerinformationen
        return spieler.getUserName() != null && !spieler.getUserName().isEmpty() &&
               spieler.getPassword() != null && !spieler.getPassword().isEmpty();
    }

    /**
     * Findet einen Benutzer anhand seines Benutzernamens.
     */
    private Spieler findUserByUsername(String userName) {
        Optional<Spieler> spielerOpt = spielerRepository.findByUserName(userName);
        return spielerOpt.orElse(null); // Gibt null zurück, wenn Benutzer nicht gefunden
    }

    /**
     * Gibt die ID des Spielers anhand seines Benutzernamens zurück.
     */
    public Long getIdByUsername(String userName) {
        Optional<Spieler> spielerOpt = spielerRepository.findByUserName(userName);
        Spieler spieler = spielerOpt.get();
        return spieler.getId();
    }

    /**
     * Gibt die Details eines Spielers anhand seines Benutzernamens zurück.
     */
    public PlayerDetailsDTO getPlayerDetailsByUsername(String username) {
        Optional<Spieler> spielerOptional = spielerRepository.findByUserName(username);

        if (spielerOptional.isEmpty()) {
            throw new RuntimeException("Spieler nicht gefunden");
        }

        Spieler spieler = spielerOptional.get();
        List<Highscore> highscores = highscoreRepository.findBySpieler(spieler);

        // Konvertiere die Highscore-Entitäten in DTOs
        List<HighscoreDTO> highscoreDTOs = highscores.stream().map(highscore -> {
            HighscoreDTO dto = new HighscoreDTO();
            dto.setDifficultyLevel(highscore.getScoreDifficultyLevel());
            dto.setContinent(highscore.getScoreContinent());
            dto.setScore(highscore.getScore());
            return dto;
        }).toList();

        PlayerDetailsDTO playerDetailsDTO = new PlayerDetailsDTO();
        playerDetailsDTO.setUsername(spieler.getUserName());
        playerDetailsDTO.setUserID(spieler.getId());
        playerDetailsDTO.setHighscores(highscoreDTOs);
        playerDetailsDTO.setGamesPlayed(spielRepository.countBySpieler(spieler));
        playerDetailsDTO.setGamesWon(spielRepository.countByWinner(spieler));

        return playerDetailsDTO;
    }

    /**
     * Speichert den Gewinner eines Spiels.
     */
    public void saveWinner(Long gameId, Long winnerId) {
        Spiel spiel = spielRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Spiel nicht gefunden: " + gameId));

        Spieler winner = spielerRepository.findById(winnerId)
                .orElseThrow(() -> new IllegalArgumentException("Spieler nicht gefunden: " + winnerId));

        spiel.setWinner(winner);
        spielRepository.save(spiel);
    }
}
