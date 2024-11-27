package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.controller;

import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.GameMoveDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.GameUpdateDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.WaitingGameDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.service.GameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class WebSocketController {

    @Autowired
    private GameService gameService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Sends the list of all waiting games to clients subscribed to "/topic/waitingGames".
     */
    @MessageMapping("/waitingGames")
    @SendTo("/topic/waitingGames")
    public List<WaitingGameDTO> getWaitingGames() {
        return gameService.getWaitingGames();
    }

    /**
     * Sends the status of a specific game to clients subscribed to "/topic/gameStatus".
     *
     * @param gameId ID of the game whose status is being requested.
     * @return The status of the game.
     */
    @MessageMapping("/gameStatus")
    @SendTo("/topic/gameStatus")
    public String getGameStatus(Long gameId) {
        return gameService.getGameStatus(gameId);
    }

    /**
     * Handles updates to waiting games and notifies all subscribed clients.
     *
     * @return Updated list of waiting games.
     */
    @MessageMapping("/updateWaitingGames")
    @SendTo("/topic/waitingGames")
    public List<WaitingGameDTO> broadcastUpdatedWaitingGames() {
        return gameService.getWaitingGames();
    }

    /**
     * Updates the game status dynamically and notifies all subscribers to "/topic/game/{gameId}".
     *
     * @param gameUpdateDTO Update object containing gameId and game state changes.
     */
    @MessageMapping("/game/update")
    public void updateGame(GameUpdateDTO gameUpdateDTO) {
        Long gameId = gameUpdateDTO.getGameId();
        messagingTemplate.convertAndSend("/topic/game/" + gameId, gameUpdateDTO);
    }

    /**
     * Processes a player's move and broadcasts the updated game state.
     *
     * @param move DTO containing details of the player's move.
     */
    @MessageMapping("/game/move")
    public void processGameMove(GameMoveDTO move) {
        GameUpdateDTO gameUpdate = gameService.makeMove(move); // Process the move and get updated state
        messagingTemplate.convertAndSend("/topic/game/" + move.getGameId(), gameUpdate);
    }
}
