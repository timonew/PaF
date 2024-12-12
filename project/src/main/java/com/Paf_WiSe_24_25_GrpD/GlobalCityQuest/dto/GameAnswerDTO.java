package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto;

public class GameAnswerDTO {
    private boolean gameAccepted;
    private Long gameId;

    // Konstruktoren
    public GameAnswerDTO() {
    }

    public GameAnswerDTO(boolean gameAccepted, Long gameId) {
        this.gameAccepted = gameAccepted;
        this.gameId = gameId;
    }

    // Getter und Setter
    public boolean isGameAccepted() {
        return gameAccepted;
    }

    public void setGameAccepted(boolean gameAccepted) {
        this.gameAccepted = gameAccepted;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }
}
