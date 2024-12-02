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
@Table(name = "map_layer") // Optional, falls ein spezifischer Tabellenname gewünscht ist
public class MapLayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "map_continent", nullable = false) // Optional: Datenbankspaltenname explizit setzen
    private String mapContinent;

    @Column(name = "map_difficulty_level", nullable = false)
    private int mapDifficultyLevel;

    @Column(name = "layer_path", nullable = false)
    private String layerPath;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMapContinent() {
		return mapContinent;
	}

	public void setMapContinent(String mapContinent) {
		this.mapContinent = mapContinent;
	}

	public int getMapDifficultyLevel() {
		return mapDifficultyLevel;
	}

	public void setMapDifficultyLevel(int mapDifficultyLevel) {
		this.mapDifficultyLevel = mapDifficultyLevel;
	}

	public void setLayerPath(String layerPath) {
		this.layerPath = layerPath;
	}

	public String getLayerPath() {
		return layerPath;
	}

}
