package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Highscore;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spieler;

public interface HighscoreRepository extends JpaRepository<Highscore, Long> {
	
    List<Highscore> findBySpieler(Spieler spieler);
    
    @Query("SELECT h FROM Highscore h WHERE h.spieler = :spieler AND h.scoreContinent = :continent AND h.scoreDifficultyLevel = :difficultyLevel")
    Optional<Highscore> findBySpielerAndScoreContinentAndScoreDifficultyLevel(
            @Param("spieler") Spieler spieler, 
            @Param("continent") String continent, 
            @Param("difficultyLevel") int difficultyLevel
    );
}


