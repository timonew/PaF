package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.controller;

import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.SimpleGameDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.service.GameService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
public class WebSocketController {

    @Autowired
    private GameService gameService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    /**
     * Sends the updated list of waiting games using SimpleGameDTO to all subscribers of "/topic/waitingGames".
     */
    @MessageMapping("/waitingGames")
    @SendTo("/topic/waitingGames")
    public List<SimpleGameDTO> getWaitingGames() {
    	System.out.println(gameService.getWaitingGames());
        return gameService.getWaitingGames();
    }

    /**
     * Triggers an update to all subscribers with the latest list of waiting games using SimpleGameDTO.
     */
    public void broadcastWaitingGames() {
        List<SimpleGameDTO> updatedGames = gameService.getWaitingGames();
        messagingTemplate.convertAndSend("/topic/waitingGames", updatedGames);
    }
   
    
    
}
