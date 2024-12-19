package com.algonet.algonetapi.controllers;

import com.algonet.algonetapi.Models.dto.UserCreationDTO;
import com.algonet.algonetapi.Models.entities.User;
import com.algonet.algonetapi.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserCreationDTO userCreationDTO){
        return authService.register(userCreationDTO);
    }
}
