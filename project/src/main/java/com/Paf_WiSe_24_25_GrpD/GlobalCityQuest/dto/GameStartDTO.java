package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto;

public class GameStartDTO {
    private int difficulty;
    private String continent;

    // Getter und Setter
    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    // toString-Methode für Debugging und Logging
    @Override
    public String toString() {
        return "GameStartDTO{" +
               "difficulty=" + difficulty + 
               ", continent='" + continent + '\'' +
               '}';
    }
}
