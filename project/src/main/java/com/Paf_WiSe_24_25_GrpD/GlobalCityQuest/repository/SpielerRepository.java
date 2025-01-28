package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository;

import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spieler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpielerRepository extends JpaRepository<Spieler, Long> {
    Optional<Spieler> findByUserName(String userName);

}