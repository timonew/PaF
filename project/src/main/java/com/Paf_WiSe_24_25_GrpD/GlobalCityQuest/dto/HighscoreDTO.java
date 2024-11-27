package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto;

import lombok.Data;

@Data
public class HighscoreDTO {
    private int difficultyLevel; // Beispiel: "easy", "medium", "hard"
    private String continent;       // Beispiel: "Europe", "Asia"
    private long score;             // Höchster Punktestand
	public int getDifficultyLevel() {
		return difficultyLevel;
	}
	public void setDifficultyLevel(int i) {
		this.difficultyLevel = i;
	}
	public String getContinent() {
		return continent;
	}
	public void setContinent(String string) {
		this.continent = string;
	}
	public long getScore() {
		return score;
	}
	public void setScore(long score) {
		this.score = score;
	}
}
