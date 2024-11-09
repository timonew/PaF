package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import  com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.RegisterRequestDTO;
import  com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spieler;
import  com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.exeption.UserNotFoundException;
import  com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository.SpielerRepository;

@Service
public class UserService {

    @Autowired
    private SpielerRepository spielerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Spieler registerUser(RegisterRequestDTO request) {
        Spieler spieler = new Spieler();
        spieler.setUsername(request.getUsername());
        spieler.setPassword(passwordEncoder.encode(request.getPassword()));
        spieler.setEmail(request.getEmail());

        return spielerRepository.save(spieler);
    }

    public Spieler findUserByUsername(String username) {
        return spielerRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

	public void loginUser() {
		// TODO Auto-generated method stub
		
	}
}

