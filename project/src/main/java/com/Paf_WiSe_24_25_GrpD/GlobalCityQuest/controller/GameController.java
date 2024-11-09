package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.GameMoveDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.service.GameService;

@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/start")
    public ResponseEntity<?> startGame() {
        return ResponseEntity.ok(gameService.startGame());
    }

    @PostMapping("/move")
    public ResponseEntity<?> makeMove(@RequestBody GameMoveDTO move) {
        return ResponseEntity.ok(gameService.makeMove(move));
    }
}
