package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto;

import lombok.Data;

import java.util.List;

@Data
public class PlayerDetailsDTO {
    private String username;
    private int gamesPlayed; // Gesamtzahl der Spiele
    private List<HighscoreDTO> highscores; // Liste der Highscores des Benutzers

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getGamesPlayed() {
		return gamesPlayed;
	}
	public void setGamesPlayed(int gamesPlayed) {
		this.gamesPlayed = gamesPlayed;
	}
	public List<HighscoreDTO> getHighscores() {
		return highscores;
	}
	public void setHighscores(List<HighscoreDTO> highscores) {
		this.highscores = highscores;
	}
}
