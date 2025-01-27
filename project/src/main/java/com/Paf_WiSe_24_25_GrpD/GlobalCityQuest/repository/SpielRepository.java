package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spiel;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spieler;

public interface SpielRepository extends JpaRepository<Spiel, Long> {

	List<Spiel> findByStatus(String string);
	
    @Query("SELECT COUNT(s) FROM Spiel s WHERE s.spieler1 = :spieler OR s.spieler2 = :spieler")
    int countBySpieler(@Param("spieler") Spieler spieler);

 
    @Query("SELECT COUNT(s) FROM Spiel s WHERE s.winner = :spieler")
    int countByWinner(@Param("spieler") Spieler spieler);
	
}

