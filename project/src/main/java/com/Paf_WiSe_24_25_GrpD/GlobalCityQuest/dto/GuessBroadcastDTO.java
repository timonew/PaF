package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto;
public class GuessBroadcastDTO {
    private Long spielZugId;
    private Long scoreSpieler1;
    private Long scoreSpieler2;

    // Getter und Setter
    public Long getSpielZugId() {
        return spielZugId;
    }

    public void setSpielZugId(Long spielZugId) {
        this.spielZugId = spielZugId;
    }

	public Long getScoreSpieler1() {
		return scoreSpieler1;
	}

	public void setScoreSpieler1(Long scoreSpieler1) {
		this.scoreSpieler1 = scoreSpieler1 ;
	}

	public Long getScoreSpieler2() {
		return scoreSpieler2;
	}

	public void setScoreSpieler2(Long scoreSpieler2) {
		this.scoreSpieler2 = scoreSpieler2;
	}
}
