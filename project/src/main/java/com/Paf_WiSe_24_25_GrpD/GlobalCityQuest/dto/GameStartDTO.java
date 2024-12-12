package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto;

public class GameStartDTO {
    private Long gameId;        // ID des Spiels
    private String username;    // Benutzername des Spielers, der das Spiel startet
    private int difficulty;     // Schwierigkeit des Spiels
    private String continent;   // Kontinent des Spiels

    // Getter und Setter für gameId
    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    // Getter und Setter für username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter und Setter für difficulty
    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    // Getter und Setter für continent
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
               "gameId=" + gameId +
               ", username='" + username + '\'' +
               ", difficulty=" + difficulty + 
               ", continent='" + continent + '\'' +
               '}';
    }
}

