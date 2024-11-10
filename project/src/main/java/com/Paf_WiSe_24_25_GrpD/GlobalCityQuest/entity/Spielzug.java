package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Spielzug {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String guessSpieler1;
   private String guessSpieler2;
   private long scoreSpieler1;
   private long scoreSpieler2;

   @ManyToOne
   @JoinColumn(name = "spiel_id")
   private Spiel spiel;

   @ManyToOne
   private Stadt stadt;

   // Getter und Setter
   public String getGuessSpieler1() {
      return guessSpieler1;
   }

   public void setGuessSpieler1(String guessSpieler1) {
      this.guessSpieler1 = guessSpieler1;
   }

   public String getGuessSpieler2() {
      return guessSpieler2;
   }

   public void setGuessSpieler2(String guessSpieler2) {
      this.guessSpieler2 = guessSpieler2;
   }

   public long getScoreSpieler1() {
      return scoreSpieler1;
   }

   public void setScoreSpieler1(long scoreSpieler1) {
      this.scoreSpieler1 = scoreSpieler1;
   }

   public long getScoreSpieler2() {
      return scoreSpieler2;
   }

   public void setScoreSpieler2(long scoreSpieler2) {
      this.scoreSpieler2 = scoreSpieler2;
   }

   public Spiel getSpiel() {
      return spiel;
   }

   public void setSpiel(Spiel spiel) {
      this.spiel = spiel;
   }

   public Stadt getStadt() {
      return stadt;
   }

   public void setStadt(Stadt stadt) {
      this.stadt = stadt;
   }
}
