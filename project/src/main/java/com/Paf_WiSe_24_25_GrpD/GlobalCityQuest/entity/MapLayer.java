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

    @Column(name = "map_continent", nullable = false) 
    private String mapContinent;
    
    @Column(name = "map_coordinates", nullable = false) 
    private String mapCoordinates;
    
    @Column(name = "map_zoom", nullable = false) 
    private int mapZoom;


	public String getMapCoordinates() {
		return mapCoordinates;
	}

	public void setMapCoordinates(String mapCoordinates) {
		this.mapCoordinates = mapCoordinates;
	}

	public int getMapZoom() {
		return mapZoom;
	}

	public void setMapZoom(int mapZoom) {
		this.mapZoom = mapZoom;
	}

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



}
