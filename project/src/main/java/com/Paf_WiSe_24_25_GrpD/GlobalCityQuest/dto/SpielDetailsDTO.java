package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto;

public class SpielDetailsDTO {

    private Long spielId;
    private String continent;
    private int difficultyLevel;
    private String status;
    private String spieler1Name;
    private String spieler2Name;
    private byte[] mapImage; // Hinzugefügt für das Bild als Byte-Array

    // Getter und Setter für alle Felder

    public Long getSpielId() {
        return spielId;
    }

    public void setSpielId(Long spielId) {
        this.spielId = spielId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
