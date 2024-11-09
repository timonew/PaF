package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity;

import java.util.Set;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;

@Entity
public class Stadt {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String name;
   private String koordinaten;
   private int difficultyLevel;
   private char continent;

   @OneToMany(mappedBy = "stadt")
   private Set<Spielzug> spielzug;

   // Getter und Setter
   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getKoordinaten() {
      return koordinaten;
   }

   public void setKoordinaten(String koordinaten) {
      this.koordinaten = koordinaten;
   }

   public int getDifficultyLevel() {
      return difficultyLevel;
   }

   public void setDifficultyLevel(int difficultyLevel) {
      this.difficultyLevel = difficultyLevel;
   }

   public char getContinent() {
      return continent;
   }

   public void setContinent(char continent) {
      this.continent = continent;
   }

   public Set<Spielzug> getSpielzug() {
      return spielzug;
   }

   public void setSpielzug(Set<Spielzug> spielzug) {
      this.spielzug = spielzug;
   }
}
