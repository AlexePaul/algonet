package com.algonet.algonetapi.services;

import com.algonet.algonetapi.exceptions.AlreadyExistingUserException;
import com.algonet.algonetapi.exceptions.NotFoundException;
import com.algonet.algonetapi.exceptions.WrongAuthException;
import com.algonet.algonetapi.models.dto.userDTOs.UserCreationDTO;
import com.algonet.algonetapi.models.dto.userDTOs.UserLoginDTO;
import com.algonet.algonetapi.models.dto.userDTOs.UserResponseDTO;
import com.algonet.algonetapi.models.entities.User;
import com.algonet.algonetapi.repositories.UserRepository;
import com.algonet.algonetapi.utils.JwtUtil;
import com.algonet.algonetapi.utils.PasswordUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class AuthService {

    private final UserRepository userRepository;    
    
    public User register(UserCreationDTO userCreationDTO, Instant createdAt) {
        log.info("Attempting to register user with username: {}, email: {}", 
            userCreationDTO.getUsername(), userCreationDTO.getEmail());
        
        if(userRepository.findByUsername(userCreationDTO.getUsername()).isPresent() || 
           userRepository.findByEmail(userCreationDTO.getEmail()).isPresent()) {
            log.warn("Registration failed - user already exists: username={}, email={}", 
                userCreationDTO.getUsername(), userCreationDTO.getEmail());
            throw new AlreadyExistingUserException();
        }
        
        if(userCreationDTO.getUsername() == null || userCreationDTO.getPassword() == null || 
           userCreationDTO.getEmail() == null) {
            log.error("Registration failed - missing required fields for user: {}", userCreationDTO.getUsername());
            throw new IllegalArgumentException();
        }
        
        User newUser = new User();
        BeanUtils.copyProperties(userCreationDTO, newUser);
        newUser.setPassword(PasswordUtil.hashPassword(newUser.getPassword()));
        newUser.setCreatedAt(createdAt);
        newUser.setRole("USER");
        
        User savedUser = userRepository.save(newUser);
        log.info("User registration successful: username={}, id={}", savedUser.getUsername(), savedUser.getId());
        return savedUser;
    }
    
    public String login(UserLoginDTO userLoginDTO){
        log.info("Login attempt for username: {}", userLoginDTO.getUsername());
        
        Optional<User> optionalUser = userRepository.findByUsername(userLoginDTO.getUsername());
        if(optionalUser.isEmpty()) {
            log.warn("Login failed - user not found: {}", userLoginDTO.getUsername());
            throw new WrongAuthException();
        }
        
        if(PasswordUtil.checkPassword(userLoginDTO.getPassword(), optionalUser.get().getPassword())) {
            String token = JwtUtil.generateToken(userLoginDTO.getUsername());
            log.info("Login successful for user: {}", userLoginDTO.getUsername());
            return token;
        } else {
            log.warn("Login failed - incorrect password for user: {}", userLoginDTO.getUsername());
            throw new WrongAuthException();
        }
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

    public UserResponseDTO getUser(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO(user.getUsername(), user.getEmail(), user.getRole(), user.getCreatedAt());
        return userResponseDTO;
    }
}
