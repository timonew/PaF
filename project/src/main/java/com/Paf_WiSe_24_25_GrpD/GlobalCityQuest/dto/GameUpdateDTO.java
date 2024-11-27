package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto;

public class GameUpdateDTO {
    
    private Long gameId;        // ID des Spiels
    private String status;      // Status des Spiels (z.B. "WAITING", "IN_PROGRESS", "FINISHED")
    private String currentState; // Aktueller Zustand oder Statusdetails

    // Getter und Setter
    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    // Optional: Konstruktoren
    public GameUpdateDTO() {
    }

    public GameUpdateDTO(Long gameId, String status, String currentState) {
        this.gameId = gameId;
        this.status = status;
        this.currentState = currentState;
    }

    @Override
    public String toString() {
        return "GameUpdateDTO{" +
                "gameId=" + gameId +
                ", status='" + status + '\'' +
                ", currentState='" + currentState + '\'' +
                '}';
    }

}
