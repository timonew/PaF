package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spielzug;

public interface SpielzugRepository extends JpaRepository<Spielzug, Long> {
    // Define additional methods to handle game moves if needed
}


