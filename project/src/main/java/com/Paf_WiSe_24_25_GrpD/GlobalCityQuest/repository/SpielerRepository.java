package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spieler;

import java.util.Optional;

public interface SpielerRepository extends JpaRepository<Spieler, Long> {
    Optional<Spieler> findByUsername(String username);
}