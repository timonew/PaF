package com.PaF_WiSe_24_25_Grp_D.Glocal.City.Quest;

public class Spielzug {
   private char guessSpieler1;
   
   public void setGuessSpieler1(char value) {
      this.guessSpieler1 = value;
   }
   
   public char getGuessSpieler1() {
      return this.guessSpieler1;
   }
   
   private char guessSpieler2;
   
   public void setGuessSpieler2(char value) {
      this.guessSpieler2 = value;
   }
   
   public char getGuessSpieler2() {
      return this.guessSpieler2;
   }
   
   private long scoreSpieler1;
   
   public void setScoreSpieler1(long value) {
      this.scoreSpieler1 = value;
   }
   
   public long getScoreSpieler1() {
      return this.scoreSpieler1;
   }
   
   private long scoreSpieler2;
   
   public void setScoreSpieler2(long value) {
      this.scoreSpieler2 = value;
   }
   
   public long getScoreSpieler2() {
      return this.scoreSpieler2;
   }
   
   /**
    * <pre>
    *           10..10     1..1
    * Spielzug ------------------------- Spiel
    *           spielzug        &lt;       spiel
    * </pre>
    */
   private Spiel spiel;
   
   public void setSpiel(Spiel value) {
      this.spiel = value;
   }
   
   public Spiel getSpiel() {
      return this.spiel;
   }
   
   /**
    * <pre>
    *           0..*     1..1
    * Spielzug ------------------------- Stadt
    *           spielzug        &gt;       stadt
    * </pre>
    */
   private Stadt stadt;
   
   public void setStadt(Stadt value) {
      this.stadt = value;
   }
   
   public Stadt getStadt() {
      return this.stadt;
   }
   
   }
