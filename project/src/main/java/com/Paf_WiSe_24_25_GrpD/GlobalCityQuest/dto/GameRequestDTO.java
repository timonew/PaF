package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto;

public class GameRequestDTO {
    private String requestingPlayer;  // Spieler, der beitreten möchte (Player 2)
    private Long gameId;             // ID des Spiels

    public GameRequestDTO(String requestingPlayer, Long gameId) {
        this.requestingPlayer = requestingPlayer;
        this.gameId = gameId;
    }

    // Getter und Setter
    public String getRequestingPlayer() {
        return requestingPlayer;
    }

    public void setRequestingPlayer(String requestingPlayer) {
        this.requestingPlayer = requestingPlayer;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }
}
