package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.service;


import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spieler;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository.SpielerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private SpielerRepository spielerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Registrierung eines neuen Benutzers mit Passwortverschlüsselung
    public Spieler registerUser(Spieler user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return spielerRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Spieler> spielerOpt = spielerRepository.findByUserName(username);
        if (spielerOpt.isEmpty()) {
            throw new UsernameNotFoundException("Benutzer nicht gefunden: " + username);
        }
        Spieler spieler = spielerOpt.get();
        return org.springframework.security.core.userdetails.User.builder()
                .username(spieler.getUserName())
                .password(spieler.getPassword())
                .roles("USER") // Rollen werden hier festgelegt, anpassbar falls benötigt
                .build();
    }
}


