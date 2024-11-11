package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "map_layer") // Optional, falls ein spezifischer Tabellenname gewünscht ist
public class MapLayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "map_continent", nullable = false) // Optional: Datenbankspaltenname explizit setzen
    private char mapContinent;

    @Column(name = "map_difficulty_level", nullable = false)
    private char mapDifficultyLevel;

    @Column(name = "layer_path", nullable = false)
    private String layerPath; 

}
