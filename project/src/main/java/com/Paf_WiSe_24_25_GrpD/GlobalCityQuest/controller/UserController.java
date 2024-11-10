package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.controller;

import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spieler;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.service.UserService;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.filter.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // POST-Login-Endpoint, der bei erfolgreichem Login ein JWT-Token zurückgibt
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Spieler loginRequest) {
        Spieler spieler = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());

        if (spieler != null) {
            String token = jwtUtil.generateToken(spieler.getUsername());
            return ResponseEntity.ok(token); // Token wird zurückgegeben
        } else {
            return ResponseEntity.status(401).body("Ungültige Zugangsdaten");
        }
    }

    // POST-Registrierung
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Spieler spieler) {
        userService.registerUser(spieler);
        return ResponseEntity.ok("Registrierung erfolgreich");
    }
}
