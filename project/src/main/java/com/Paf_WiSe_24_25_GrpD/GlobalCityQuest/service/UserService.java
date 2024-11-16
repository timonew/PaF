package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.service;

import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.config.AuthStatus;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.config.RegistrationStatus;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spieler;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.repository.SpielerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final SpielerRepository spielerRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(SpielerRepository spielerRepository, PasswordEncoder passwordEncoder) {
        this.spielerRepository = spielerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Registrierung eines neuen Benutzers mit Passwortverschlüsselung und Validierung
    public RegistrationStatus registerUser(Spieler spieler) {
        // Überprüfen, ob der Benutzer bereits existiert
        if (isUserExists(spieler.getUsername())) {
            return RegistrationStatus.USER_ALREADY_EXISTS;
        }

        // Validierung der Benutzerdaten
        if (!isValid(spieler)) {
            return RegistrationStatus.INVALID_DATA;
        }

        // Passwort verschlüsseln und Benutzer speichern
        spieler.setPassword(passwordEncoder.encode(spieler.getPassword()));
        spielerRepository.save(spieler);
        return RegistrationStatus.SUCCESS;
    }

    // Authentifizierungsmethode
    public AuthStatus authenticate(String username, String password) {
        Spieler spieler = findUserByUsername(username);

        if (spieler == null) {
            return AuthStatus.USER_NOT_FOUND;
        }

        // Passwortüberprüfung
        if (!passwordEncoder.matches(password, spieler.getPassword())) {
            return AuthStatus.INVALID_CREDENTIALS;
        }

        return AuthStatus.SUCCESS;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Spieler> spielerOpt = spielerRepository.findByUserName(username);
        if (spielerOpt.isEmpty()) {
            throw new UsernameNotFoundException("Benutzer nicht gefunden: " + username);
        }
        Spieler spieler = spielerOpt.get();
        return org.springframework.security.core.userdetails.User.builder()
                .username(spieler.getUsername())
                .password(spieler.getPassword())
                .roles("USER") // Hier können zusätzliche Rollen hinzugefügt werden
                .build();
    }

    private boolean isUserExists(String username) {
        return spielerRepository.findByUserName(username).isPresent();
    }

    private boolean isValid(Spieler spieler) {
        // Hier kann eine erweiterte Validierung der Benutzerinformationen erfolgen
        return spieler.getUsername() != null && !spieler.getUsername().isEmpty() &&
               spieler.getPassword() != null && !spieler.getPassword().isEmpty();
    }

    private Spieler findUserByUsername(String username) {
        Optional<Spieler> spielerOpt = spielerRepository.findByUserName(username);
        return spielerOpt.orElse(null); // Gibt null zurück, wenn der Benutzer nicht gefunden wurde
    }
}
