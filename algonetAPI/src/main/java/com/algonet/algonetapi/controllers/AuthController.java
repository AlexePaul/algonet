package com.algonet.algonetapi.controllers;

import com.algonet.algonetapi.annotations.GetAuthUser;
import com.algonet.algonetapi.models.dto.userDTOs.UserCreationDTO;
import com.algonet.algonetapi.models.dto.userDTOs.UserLoginDTO;
import com.algonet.algonetapi.models.dto.userDTOs.UserResponseDTO;
import com.algonet.algonetapi.models.entities.User;
import com.algonet.algonetapi.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserCreationDTO userCreationDTO){
        return new ResponseEntity<>(authService.register(userCreationDTO, Instant.now()), HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDTO userLoginDTO){
        return new ResponseEntity<>(authService.login(userLoginDTO), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<UserResponseDTO> getUser(@GetAuthUser User user){
        return new ResponseEntity<>(authService.getUser(user), HttpStatus.OK);
    }

    @PutMapping("/makeUploader")
    public ResponseEntity<User> makeUploader(Integer id){
        return new ResponseEntity<>(authService.makeUploader(id), HttpStatus.OK);
    }
}
