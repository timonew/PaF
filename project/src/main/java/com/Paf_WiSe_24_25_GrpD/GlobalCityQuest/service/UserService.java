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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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


    // Registrierung eines neuen Benutzers mit Passwortverschlüsselung und Validierung
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

    private void saveUser(Spieler spieler) {
        spielerRepository.save(spieler);
    }

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

    private boolean isUserExists(String userName) {
        return spielerRepository.findByUserName(userName).isPresent();
    }

    private boolean isValid(Spieler spieler) {
        // Validierung der Benutzerinformationen
        return spieler.getUserName() != null && !spieler.getUserName().isEmpty() &&
               spieler.getPassword() != null && !spieler.getPassword().isEmpty();
    }

    private Spieler findUserByUsername(String userName) {
        Optional<Spieler> spielerOpt = spielerRepository.findByUserName(userName);
        return spielerOpt.orElse(null); // Gibt null zurück, wenn Benutzer nicht gefunden
    }

    public Long getIdByUsername(String userName) {
        Optional<Spieler> spielerOpt = spielerRepository.findByUserName(userName);
        Spieler spieler = spielerOpt.get();
        return spieler.getId();
    }
    
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

	public void saveWinner(Long gameId, Long winnerId) {
		 Spiel spiel = spielRepository.findById(gameId)
	                .orElseThrow(() -> new IllegalArgumentException("Spiel nicht gefunden: " + gameId));
		 
		Spieler winner = spielerRepository.findById(winnerId)
				.orElseThrow(() -> new IllegalArgumentException("Spieler nicht gefunden: " + winnerId));
		 
			 spiel.setWinner(winner);
			 spielRepository.save(spiel);
		 
		 
		 
		
	}
}
