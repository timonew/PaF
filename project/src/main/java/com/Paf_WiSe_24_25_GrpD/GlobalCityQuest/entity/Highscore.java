package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity;

public class Highscore {
   private int scoreDifficultyLevel;
   
   public void setScoreDifficultyLevel(int value) {
      this.scoreDifficultyLevel = value;
   }
   
   public int getScoreDifficultyLevel() {
      return this.scoreDifficultyLevel;
   }
   
   private char scoreContinent;
   
   public void setScoreContinent(char value) {
      this.scoreContinent = value;
   }
   
   public char getScoreContinent() {
      return this.scoreContinent;
   }
   
   private long score;
   
   public void setScore(long value) {
      this.score = value;
   }
   
   public long getScore() {
      return this.score;
   }
   
   }
