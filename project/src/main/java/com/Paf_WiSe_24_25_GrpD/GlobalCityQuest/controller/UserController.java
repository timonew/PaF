package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.RegisterRequestDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.LoginRequestDTO;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.service.UserService;




@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequestDTO request) {
        userService.registerUser(request);
        return ResponseEntity.ok("User registered successfully");
    }
    
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody @Validated LoginRequestDTO loginRequest) {
    	userService.loginUser();
        return ResponseEntity.ok("User registered successfully");
    }

}


