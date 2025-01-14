package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.controller;

import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.SimpleGameDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spielzug;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.GameAnswerDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.GameRequestDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.GuessBroadcastDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.ScoreBroadcastDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.GameInitDTO;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Sendet die Liste der wartenden Spiele an alle Clients.
     *
     * @param waitingGames Liste der wartenden Spiele
     */
    public void sendWaitingGames(List<SimpleGameDTO> waitingGames) {
        messagingTemplate.convertAndSend("/topic/waitingGames", waitingGames);
    }

    /**
     * Sendet eine Spielanfrage an einen spezifischen Benutzer.
     *
     * @param userID      ID des Benutzers, der die Nachricht erhalten soll
     * @param gameRequest Die Spielanfrage-Daten
     */
    public void sendGameRequestToUser(Long userID, GameRequestDTO gameRequest) {
        String destination = String.format("/topic/user/%d/gameRequests", userID);
        messagingTemplate.convertAndSend(destination, gameRequest);
    }
    
    /**
     * Sendet eine Spielanfrage an einen spezifischen Benutzer.
     *
     * @param userID      ID des Benutzers, der die Nachricht erhalten soll
     * @param gameAnswerDTO Die Spielanfrage-Daten
     */
    public void sendGameAnswerToUser(GameAnswerDTO gameAnswerDTO, Long userID) {
        String destination = String.format("/topic/user/%d/gameRequestsAnswer", userID);
        messagingTemplate.convertAndSend(destination, gameAnswerDTO);
    }
    
    public void broadcastGuess(Long gameId, GuessBroadcastDTO guessDTO) {
        String destination = "/topic/game/" + gameId + "/guess";
        messagingTemplate.convertAndSend(destination, guessDTO);
        System.out.println("Guess-Daten über WebSocket gesendet: " + guessDTO);
    }
    
    public void broadcastScore(Long gameId, ScoreBroadcastDTO scoreDTO) {
        String destination = "/topic/game/" + gameId + "/score";
        messagingTemplate.convertAndSend(destination, scoreDTO);
    }

}