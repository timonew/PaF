package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto;

import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spieler;

public class WaitingGameDTO {
    private String spieler1Name;
    private int difficultyLevel;
    private String continent;

    public WaitingGameDTO() {}

    public WaitingGameDTO(String spieler1Name, int difficultyLevel, String continent) {
        this.spieler1Name = spieler1Name;
        this.difficultyLevel = difficultyLevel;
        this.continent = continent;
    }

    public String getSpieler1Name() {
        return spieler1Name;
    }

    public void setSpieler1Name(String spieler1Name) {
        this.spieler1Name = spieler1Name;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    @Override
    public String toString() {
        return "WaitingGameDTO{" +
               "spieler1Name='" + spieler1Name + '\'' +
               ", difficultyLevel='" + difficultyLevel + '\'' +
               ", continent='" + continent + '\'' +
               '}';
    }

	public void setGameId(Long id) {
		// TODO Auto-generated method stub
		
	}

	public void setStatus(String status) {
		// TODO Auto-generated method stub
		
	}
}

