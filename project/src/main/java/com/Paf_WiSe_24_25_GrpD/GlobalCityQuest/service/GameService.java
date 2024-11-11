package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.GameMoveDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.GameStartDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spiel;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spielzug;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository.SpielRepository;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository.SpielzugRepository;

@Service
public class GameService {

    @Autowired
    private SpielRepository gameRepository;

    @Autowired
    private SpielzugRepository spielzugRepository;

    public Spiel startGame(GameStartDTO gameStartDTO) {
        Spiel spiel = new Spiel();
       
        spiel.setDifficulty("Medium");
        return gameRepository.save(spiel);
    }

 	public Object makeMove(GameMoveDTO move) {
		return spielzugRepository.save(move);
	}


	public Object joinGame(Long gameId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getGameStatus(Long gameId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object endGame(Long gameId) {
		// TODO Auto-generated method stub
		return null;
	}
    }
