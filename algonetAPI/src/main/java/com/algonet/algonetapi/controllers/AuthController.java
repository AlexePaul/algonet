package com.algonet.algonetapi.controllers;

import com.algonet.algonetapi.Models.dto.UserCreationDTO;
import com.algonet.algonetapi.Models.dto.UserLoginDTO;
import com.algonet.algonetapi.Models.entities.User;
import com.algonet.algonetapi.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {
    private AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserCreationDTO userCreationDTO){
        return new ResponseEntity<>(authService.register(userCreationDTO), HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDTO userLoginDTO){
        return new ResponseEntity<>(authService.login(userLoginDTO), HttpStatus.OK);
    }
}
