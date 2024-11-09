package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.MapLayer;

public interface MapLayerRepository extends JpaRepository<MapLayer, Long> {
    // Custom queries for map layers if needed
}

