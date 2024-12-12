package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Stadt;

public interface StadtRepository extends JpaRepository<Stadt, Long> {

	 List<Stadt> findByContinentAndDifficultyLevel(String continent, int difficultyLevel);
	}
   

