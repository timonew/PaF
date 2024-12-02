package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto;

public class UpdateGameStatusDTO {
    private Long gameId;
    private String newStatus;

    // Getter und Setter
    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }
}
