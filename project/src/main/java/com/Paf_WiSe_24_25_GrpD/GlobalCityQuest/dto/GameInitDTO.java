package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto;

import java.util.List;
import java.util.Set;

import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spielzug;

public class GameInitDTO {
	private Long spieler1Id;
	private String spieler1Name;
	private String spieler2Name;
	private Long spieler2Id;
    private Long gameId;
    private int difficultyLevel;
    private String status;
    private String continent;
    private List<SpielzugDTO> spielzuege;

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

    public List<SpielzugDTO> getSpielzuege() {
        return spielzuege;
    }

    public void setSpielzuege(List<SpielzugDTO> spielzuege) {
        this.spielzuege = spielzuege;
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

	public Long getSpieler1Id() {
		return spieler1Id;
	}

	public void setSpieler1Id(Long spieler1Id) {
		this.spieler1Id = spieler1Id;
	}

	public Long getSpieler2Id() {
		return spieler2Id;
	}

	public void setSpieler2Id(Long spieler2Id) {
		this.spieler2Id = spieler2Id;
	}

	public String getSpieler1Name() {
		return spieler1Name;
	}

	public void setSpieler1Name(String spieler1Name) {
		this.spieler1Name = spieler1Name;
	}

	public String getSpieler2Name() {
		return spieler2Name;
	}

	public void setSpieler2Name(String spieler2Name) {
		this.spieler2Name = spieler2Name;
	}


}
