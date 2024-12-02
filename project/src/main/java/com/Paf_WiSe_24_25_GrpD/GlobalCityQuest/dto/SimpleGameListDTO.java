package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto;

import java.util.List;

public class SimpleGameListDTO {
    private List<SimpleGameDTO> waitingGames;

    public SimpleGameListDTO() {}

    public SimpleGameListDTO(List<SimpleGameDTO> waitingGames) {
        this.waitingGames = waitingGames;
    }

    public List<SimpleGameDTO> getWaitingGames() {
        return waitingGames;
    }

    public void setWaitingGames(List<SimpleGameDTO> waitingGames) {
        this.waitingGames = waitingGames;
    }
}
