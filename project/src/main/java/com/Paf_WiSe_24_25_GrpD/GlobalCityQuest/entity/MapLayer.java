package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class MapLayer {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   
   private char mapContinent;
   private char mapDifficultyLevel;
   private String layerPath; // Ändern auf String, um Dateipfade zu speichern

   // Getter und Setter
   public char getMapContinent() {
      return mapContinent;
   }

   public void setMapContinent(char mapContinent) {
      this.mapContinent = mapContinent;
   }

   public char getMapDifficultyLevel() {
      return mapDifficultyLevel;
   }

   public void setMapDifficultyLevel(char mapDifficultyLevel) {
      this.mapDifficultyLevel = mapDifficultyLevel;
   }

   public String getLayerPath() {
      return layerPath;
   }

   public void setLayerPath(String layerPath) {
      this.layerPath = layerPath;
   }
}

