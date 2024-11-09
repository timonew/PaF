package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Stadt;

public interface StadtRepository extends JpaRepository<Stadt, Long> {
    // Additional queries for cities if needed
}

