package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto;

public class WaitingGameDTO {
    private String player1Name;
    private String difficulty;
    private String continent;
	public String getPlayer1Name() {
		return player1Name;
	}
	public void setPlayer1Name(String player1Name) {
		this.player1Name = player1Name;
	}
	public String getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(String object) {
		this.difficulty = object;
	}
	public String getContinent() {
		return continent;
	}
	public void setContinent(String continent) {
		this.continent = continent;
	}
	public void setGameId(Long id) {
		// TODO Auto-generated method stub
		
	}



}
