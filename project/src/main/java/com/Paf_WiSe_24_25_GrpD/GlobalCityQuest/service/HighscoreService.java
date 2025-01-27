package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Highscore;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spiel;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spieler;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spielzug;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository.HighscoreRepository;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository.SpielRepository;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository.SpielerRepository;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository.SpielzugRepository;

import java.util.List;
import java.util.Optional;

@Service
public class HighscoreService {

    @Autowired
    private HighscoreRepository highscoreRepository;
    
    @Autowired
    private SpielRepository spielRepository;

    
    @Autowired
    private SpielzugRepository spielzugRepository;
    
    

    public List<Highscore> getAllHighscores() {
        return highscoreRepository.findAll();
    }
    
    
	public void saveHighscore(Long gameId) {
        Spiel spiel = spielRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Spiel nicht gefunden: " + gameId));
	  
        Spieler spieler1 = spiel.getSpieler1();
        Spieler spieler2 = spiel.getSpieler2();
        
        String continent = spiel.getContinent();
        int difficultylevel = spiel.getDifficultyLevel();
        
        Long gameScoreSpieler1 = calculateTotalScoreForPlayer(gameId,true);
        Long gameScoreSpieler2 = calculateTotalScoreForPlayer(gameId,false);
        
        Optional<Highscore> optionalHighscoreSpieler1 = highscoreRepository.findBySpielerAndScoreContinentAndScoreDifficultyLevel(spieler1,continent,difficultylevel);
        Optional<Highscore> optionalHighscoreSpieler2 = highscoreRepository.findBySpielerAndScoreContinentAndScoreDifficultyLevel(spieler2,continent,difficultylevel);
        
        if (optionalHighscoreSpieler1.isPresent()) {
            Highscore highscoreSpieler1 = optionalHighscoreSpieler1.get();
            System.out.println("Highscore Spieler 1 gefunden.");
            if (gameScoreSpieler1 > highscoreSpieler1.getScore()) {
            	System.out.println("Highscore Spieler 1 wird aktualisiert.");
                highscoreSpieler1.setScore(gameScoreSpieler1);
                highscoreRepository.save(highscoreSpieler1);
            }
        } else {
            // Neuen Highscore für Spieler 1 erstellen
            Highscore newHighscoreSpieler1 = new Highscore();
            newHighscoreSpieler1.setSpieler(spieler1);
            newHighscoreSpieler1.setScoreContinent(continent);
            newHighscoreSpieler1.setScoreDifficultyLevel(difficultylevel);
            newHighscoreSpieler1.setScore(gameScoreSpieler1);
            highscoreRepository.save(newHighscoreSpieler1);
        }

        if (optionalHighscoreSpieler2.isPresent()) {
            Highscore highscoreSpieler2 = optionalHighscoreSpieler2.get();
            System.out.println("Highscore Spieler 2 gefunden.");
            if (gameScoreSpieler2 > highscoreSpieler2.getScore()) {
            	System.out.println("Highscore Spieler 2 wird aktualisiert.");
                highscoreSpieler2.setScore(gameScoreSpieler2);
                highscoreRepository.save(highscoreSpieler2);
            }
        } else {
            // Neuen Highscore für Spieler 2 erstellen
            Highscore newHighscoreSpieler2 = new Highscore();
            newHighscoreSpieler2.setSpieler(spieler2);
            newHighscoreSpieler2.setScoreContinent(continent);
            newHighscoreSpieler2.setScoreDifficultyLevel(difficultylevel);
            newHighscoreSpieler2.setScore(gameScoreSpieler2);
            highscoreRepository.save(newHighscoreSpieler2);
        }
        
        spiel.setStatus("FINISHED");
        spielRepository.save(spiel);
        
	  
}


  // Methode zur Berechnung des Gesamtscores private long
 private long calculateTotalScoreForPlayer(Long gameId, boolean isPlayer1) {
	  List<Spielzug> moves = spielzugRepository.findBySpielId(gameId);
	  return moves.stream() .mapToLong(move -> isPlayer1 ? move.getScoreSpieler1()
	  : move.getScoreSpieler2()) .sum(); 
	  }
 

}

