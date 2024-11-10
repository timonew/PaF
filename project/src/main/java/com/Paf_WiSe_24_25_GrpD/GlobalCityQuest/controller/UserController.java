package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.controller;

import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity.Spieler;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // GET-Login-Seite
    @GetMapping("/login")
    public String login() {
        return "login"; // Login-HTML-Seite wird geladen
    }

    // GET-Registrierungsseite
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("spieler", new Spieler()); // Neues Spieler-Objekt für Formular
        return "register"; // Register-HTML-Seite wird geladen
    }

    // POST-Registrierung
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("spieler") Spieler spieler) {
        userService.registerUser(spieler); // Benutzer registrieren
        return "redirect:/login"; // Nach Registrierung zur Login-Seite weiterleiten
    }
}


