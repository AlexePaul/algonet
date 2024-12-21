package com.algonet.algonetapi.services;

import com.algonet.algonetapi.Models.dto.UserCreationDTO;
import com.algonet.algonetapi.Models.dto.UserLoginDTO;
import com.algonet.algonetapi.Models.entities.User;
import com.algonet.algonetapi.exceptions.WrongAuthException;
import com.algonet.algonetapi.repositories.UserRepository;
import com.algonet.algonetapi.utils.JwtUtil;
import com.algonet.algonetapi.utils.PasswordUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public User register(UserCreationDTO userCreationDTO) {
        User newUser = new User();
        BeanUtils.copyProperties(userCreationDTO, newUser);
        newUser.setPassword(PasswordUtil.hashPassword(newUser.getPassword()));
        newUser.setCreated_at(Instant.now());
        newUser.setRole("User");
        userRepository.save(newUser);
        return newUser;
    }

    public String login(UserLoginDTO userLoginDTO){
        Optional<User> optionalUser = userRepository.findByUsername(userLoginDTO.getUsername());
        if(optionalUser.isEmpty())
            throw new WrongAuthException();
        if(PasswordUtil.checkPassword(userLoginDTO.getPassword(),optionalUser.get().getPassword()))
            return JwtUtil.generateToken(userLoginDTO.getUsername());
        else
            throw new WrongAuthException();
    }
}
