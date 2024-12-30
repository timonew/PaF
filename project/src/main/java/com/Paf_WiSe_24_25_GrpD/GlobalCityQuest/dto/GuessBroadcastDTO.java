package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto;
public class GuessBroadcastDTO {
    private Long spielZugId;
    private Long scoreSpieler1;
    private Long scoreSpieler2;
    private String guessSpieler1;
    private String guessSpieler2;

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

	public String getGuessSpieler1() {
		return guessSpieler1;
	}

	public void setGuessSpieler1(String guessSpieler1) {
		this.guessSpieler1 = guessSpieler1;
	}

	public String getGuessSpieler2() {
		return guessSpieler2;
	}

	public void setGuessSpieler2(String guessSpieler2) {
		this.guessSpieler2 = guessSpieler2;
	}
}
