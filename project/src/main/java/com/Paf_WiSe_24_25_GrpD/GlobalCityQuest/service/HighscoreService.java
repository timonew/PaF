package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Highscore;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository.HighscoreRepository;

import java.util.List;

@Service
public class HighscoreService {

    @Autowired
    private HighscoreRepository highscoreRepository;

    public List<Highscore> getAllHighscores() {
        return highscoreRepository.findAll();
    }

    public Highscore saveHighscore(Highscore highscore) {
        return highscoreRepository.save(highscore);
    }
}

