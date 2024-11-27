package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Highscore;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spieler;

public interface HighscoreRepository extends JpaRepository<Highscore, Long> {
    List<Highscore> findBySpieler(Spieler spieler);
}


