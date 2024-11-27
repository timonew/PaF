package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spiel;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spieler;

public interface SpielRepository extends JpaRepository<Spiel, Long> {

	Optional<Spieler> findByStatus(String status);
}

