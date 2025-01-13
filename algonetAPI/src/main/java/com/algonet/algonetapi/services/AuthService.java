package com.algonet.algonetapi.services;

import com.algonet.algonetapi.exceptions.AlreadyExistingUserException;
import com.algonet.algonetapi.exceptions.NotFoundException;
import com.algonet.algonetapi.exceptions.WrongAuthException;
import com.algonet.algonetapi.models.dto.userDTOs.UserCreationDTO;
import com.algonet.algonetapi.models.dto.userDTOs.UserLoginDTO;
import com.algonet.algonetapi.models.entities.User;
import com.algonet.algonetapi.repositories.UserRepository;
import com.algonet.algonetapi.utils.JwtUtil;
import com.algonet.algonetapi.utils.PasswordUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;

    public User register(UserCreationDTO userCreationDTO, Instant createdAt) {
        if(userRepository.findByUsername(userCreationDTO.getUsername()).isPresent() || userRepository.findByEmail(userCreationDTO.getEmail()).isPresent())
            throw new AlreadyExistingUserException();
        if(userCreationDTO.getUsername() == null || userCreationDTO.getPassword() == null || userCreationDTO.getEmail() == null)
            throw new IllegalArgumentException();
        User newUser = new User();
        BeanUtils.copyProperties(userCreationDTO, newUser);
        newUser.setPassword(PasswordUtil.hashPassword(newUser.getPassword()));
        newUser.setCreatedAt(createdAt);
        newUser.setRole("USER");
        return userRepository.save(newUser);
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

    public User getUserByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty())
            throw new NotFoundException();
        return optionalUser.get();
    }

    public User makeUploader(Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty())
            throw new NotFoundException();
        User user = optionalUser.get();
        user.setRole("UPLOADER");
        userRepository.save(user);
        return user;
    }
}
