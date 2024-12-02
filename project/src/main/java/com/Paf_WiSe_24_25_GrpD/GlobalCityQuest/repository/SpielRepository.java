package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spiel;

public interface SpielRepository extends JpaRepository<Spiel, Long> {

	List<Spiel> findByStatus(String string);
	
}

