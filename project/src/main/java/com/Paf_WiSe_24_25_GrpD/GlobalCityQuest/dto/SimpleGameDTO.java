package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto;

public class SimpleGameDTO {
    private Long id;
    private String spieler1Name;
    private Long spieler1ID;
    private String continent;
    private int difficultyLevel;

    // Getter und Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpieler1Name() {
        return spieler1Name;
    }

    public void setSpieler1Name(String spieler1Name) {
        this.spieler1Name = spieler1Name;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

	public Long getSpieler1ID() {
		return spieler1ID;
	}

	public void setSpieler1ID(Long long1) {
				this.spieler1ID = long1;
	}
	
	@Override
	public String toString() {
	    return "SimpleGameDTO{" +
	           "id=" + id +
	           ", spieler1Name='" + spieler1Name + '\'' +
	           ", spieler1ID=" + spieler1ID +
	           ", continent='" + continent + '\'' +
	           ", difficultyLevel=" + difficultyLevel +
	           '}';
	}

}
