package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto;
public class SubmitGuessDTO {
    private Long gameId;      // ID des Spiels
    private Long spielerId;   // ID des Spielers
    private String guess;     // Wert des Guesses
	public Long getGameId() {
		return gameId;
	}
	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}
	public Long getSpielerId() {
		return spielerId;
	}
	public void setSpielerId(Long spielerId) {
		this.spielerId = spielerId;
	}
	public String getGuess() {
		return guess;
	}
	public void setGuess(String guess) {
		this.guess = guess;
	}

    // Getter und Setter
}
