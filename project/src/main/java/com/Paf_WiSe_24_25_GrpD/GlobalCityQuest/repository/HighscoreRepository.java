package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Highscore;

public interface HighscoreRepository extends JpaRepository<Highscore, Long> {
    // Additional query methods for highscores
}


