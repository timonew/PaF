/**
 * Controller zur Bereistellung der Websockets.
 * 
 * @author Timo Neuwerk
 * @date 01.02.2025
 */
package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.controller;

import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.SimpleGameDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.GameAnswerDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.GameRequestDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.GuessBroadcastDTO;
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
     */
    public void sendWaitingGames(List<SimpleGameDTO> waitingGames) {
        messagingTemplate.convertAndSend("/topic/waitingGames", waitingGames);
    }

    /**
     * Sendet eine Spielanfrage an einen spezifischen Benutzer.
     */
    public void sendGameRequestToUser(Long userID, GameRequestDTO gameRequest) {
        String destination = String.format("/topic/user/%d/gameRequests", userID);
        messagingTemplate.convertAndSend(destination, gameRequest);
    }
    
    /**
     * Sendet eine Spielantwort an einen spezifischen Benutzer.
     */
    public void sendGameAnswerToUser(GameAnswerDTO gameAnswerDTO, Long userID) {
        String destination = String.format("/topic/user/%d/gameRequestsAnswer", userID);
        messagingTemplate.convertAndSend(destination, gameAnswerDTO);
    }
    
    /**
     * Gibt die aktuellen Guesses aus.(für Spielzugtabelle aus der Gameseite)
     */
    public void broadcastGuess(Long gameId, GuessBroadcastDTO guessDTO) {
        String destination = "/topic/game/" + gameId + "/guess";
        messagingTemplate.convertAndSend(destination, guessDTO);
        System.out.println("Guess-Daten über WebSocket gesendet: " + guessDTO);
    }
    

}