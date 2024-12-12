package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto;

import java.util.List;
import java.util.Set;

import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spielzug;

public class GameInitDTO {
    private Long gameId;
    private String status;
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


}
