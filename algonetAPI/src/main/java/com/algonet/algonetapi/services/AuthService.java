package com.algonet.algonetapi.services;

import com.algonet.algonetapi.Models.dto.UserCreationDTO;
import com.algonet.algonetapi.Models.entities.User;
import com.algonet.algonetapi.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public ResponseEntity<User> register(UserCreationDTO userCreationDTO) {
        User newUser = new User();
        BeanUtils.copyProperties(userCreationDTO, newUser);
        newUser.setCreated_at(Instant.now());
        newUser.setRole("User");
        userRepository.save(newUser);
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }
}
