package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto;

public class SpielzugDTO {
	private Long spielZugId;
    private Long stadtId;
    private String stadtName;
    private String koordinaten;

    // Getter und Setter
    public Long getStadtId() {
        return stadtId;
    }

    public void setStadtId(Long stadtId) {
        this.stadtId = stadtId;
    }

    public String getStadtName() {
        return stadtName;
    }

    public void setStadtName(String stadtName) {
        this.stadtName = stadtName;
    }

    public String getKoordinaten() {
        return koordinaten;
    }

    public void setKoordinaten(String koordinaten) {
        this.koordinaten = koordinaten;
    }

	public Long getSpielZugId() {
		return spielZugId;
	}

	public void setSpielZugId(Long spielZugId) {
		this.spielZugId = spielZugId;
	}
}
