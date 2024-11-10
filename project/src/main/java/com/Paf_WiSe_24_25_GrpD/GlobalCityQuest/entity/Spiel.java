package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity;

import java.util.Set;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Spiel {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private char continent;
   private int difficultyLevel;

   @ManyToOne
   @JoinColumn(name = "spieler1_id")
   private Spieler spieler1;

   @ManyToOne
   @JoinColumn(name = "spieler2_id")
   private Spieler spieler2;

   @OneToMany(mappedBy = "spiel")
   private Set<Spielzug> spielzug;

   @OneToMany
   private Set<MapLayer> mapLayer;

   // Getter und Setter
   public char getContinent() {
      return continent;
   }

   public void setContinent(char continent) {
      this.continent = continent;
   }

   public int getDifficultyLevel() {
      return difficultyLevel;
   }

   public void setDifficultyLevel(int difficultyLevel) {
      this.difficultyLevel = difficultyLevel;
   }

   public Spieler getSpieler1() {
      return spieler1;
   }

   public void setSpieler1(Spieler spieler1) {
      this.spieler1 = spieler1;
   }

   public Spieler getSpieler2() {
      return spieler2;
   }

   public void setSpieler2(Spieler spieler2) {
      this.spieler2 = spieler2;
   }

   public Set<Spielzug> getSpielzug() {
      return spielzug;
   }

   public void setSpielzug(Set<Spielzug> spielzug) {
      this.spielzug = spielzug;
   }

   public Set<MapLayer> getMapLayer() {
      return mapLayer;
   }

   public void setMapLayer(Set<MapLayer> mapLayer) {
      this.mapLayer = mapLayer;
   }

	public void setRounds(int i) {
		// TODO Auto-generated method stub
		
	}

	public void setDifficulty(String string) {
		// TODO Auto-generated method stub
		
	}

}
