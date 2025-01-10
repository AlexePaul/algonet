package com.algonet.algonetapi.unit;

import com.algonet.algonetapi.exceptions.AlreadyExistingUserException;
import com.algonet.algonetapi.exceptions.NotFoundException;
import com.algonet.algonetapi.exceptions.WrongAuthException;
import com.algonet.algonetapi.models.dto.userDTOs.UserCreationDTO;
import com.algonet.algonetapi.models.dto.userDTOs.UserLoginDTO;
import com.algonet.algonetapi.models.entities.User;
import com.algonet.algonetapi.repositories.UserRepository;
import com.algonet.algonetapi.services.AuthService;
import com.algonet.algonetapi.utils.JwtUtil;
import com.algonet.algonetapi.utils.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    @DisplayName("Register user successfully")
    void registerUserSuccessfully() {
        Instant fixedTime = Instant.parse("2025-01-04T10:00:00Z");
        UserCreationDTO userCreationDTO = new UserCreationDTO("username", "password", "email");
        User newUser = new User();
        BeanUtils.copyProperties(userCreationDTO, newUser);
        newUser.setPassword(PasswordUtil.hashPassword(newUser.getPassword()));
        newUser.setCreatedAt(fixedTime);
        newUser.setRole("USER");
        newUser.setId(1);

        when(userRepository.findByUsername(userCreationDTO.getUsername())).thenReturn(Optional.empty());
        when(userRepository.save(newUser)).thenReturn(newUser);

        User registeredUser = authService.register(userCreationDTO, fixedTime);
        registeredUser.setId(1);

        assertTrue(PasswordUtil.checkPassword(userCreationDTO.getPassword(), registeredUser.getPassword()));
        newUser.setPassword(registeredUser.getPassword());
        assertEquals(newUser, registeredUser);

        verify(userRepository, times(1)).findByUsername(userCreationDTO.getUsername());
    }

    @Test
    @DisplayName("Register user with existing username")
    void registerUserWithExistingUsername() {
        UserCreationDTO userCreationDTO = new UserCreationDTO("username", "password", "email");

        when(userRepository.findByUsername(userCreationDTO.getUsername())).thenReturn(Optional.of(new User()));

        assertThrows(AlreadyExistingUserException.class, () -> authService.register(userCreationDTO, Instant.now()));

        verify(userRepository, times(1)).findByUsername(userCreationDTO.getUsername());
    }


    @Test
    @DisplayName("Register with illegal arguments")
    void registerWithIllegalArgumentsUsername() {
        UserCreationDTO userCreationDTO = new UserCreationDTO(null, "password", "email");

        assertThrows(IllegalArgumentException.class, () -> authService.register(userCreationDTO, Instant.now()));
    }

    @Test
    @DisplayName("Register with illegal arguments")
    void registerWithIllegalArgumentsPassword() {
        UserCreationDTO userCreationDTO = new UserCreationDTO("username", null, "email");

        assertThrows(IllegalArgumentException.class, () -> authService.register(userCreationDTO, Instant.now()));
    }

    @Test
    @DisplayName("Register with illegal arguments")
    void registerWithIllegalArgumentsEmail() {
        UserCreationDTO userCreationDTO = new UserCreationDTO("username", "password", null);

        assertThrows(IllegalArgumentException.class, () -> authService.register(userCreationDTO, Instant.now()));
    }

    @Test
    @DisplayName("Login user successfully")
    void loginUserSuccessfully() {
        UserLoginDTO userLoginDTO = new UserLoginDTO("username", "password");
        User user = new User();
        user.setUsername(userLoginDTO.getUsername());
        user.setPassword(PasswordUtil.hashPassword(userLoginDTO.getPassword()));

        when(userRepository.findByUsername(userLoginDTO.getUsername())).thenReturn(Optional.of(user));

        try (MockedStatic<JwtUtil> jwtUtilMockedStatic = mockStatic(JwtUtil.class)) {
            jwtUtilMockedStatic.when(() -> JwtUtil.generateToken(userLoginDTO.getUsername())).thenReturn("token");

            String token = authService.login(userLoginDTO);

            assertEquals("token", token);
        }

        verify(userRepository, times(1)).findByUsername(userLoginDTO.getUsername());
    }

    @Test
    @DisplayName("Login with wrong username")
    void loginWithWrongUsername() {
        UserLoginDTO userLoginDTO = new UserLoginDTO("username", "password");

        when(userRepository.findByUsername(userLoginDTO.getUsername())).thenReturn(Optional.empty());

        assertThrows(WrongAuthException.class, () -> authService.login(userLoginDTO));

        verify(userRepository, times(1)).findByUsername(userLoginDTO.getUsername());
    }

    @Test
    @DisplayName("Login with wrong password")
    void loginWithWrongPassword() {
        UserLoginDTO userLoginDTO = new UserLoginDTO("username", "password");
        User user = new User();
        user.setUsername(userLoginDTO.getUsername());
        user.setPassword(PasswordUtil.hashPassword("wrongPassword"));

        when(userRepository.findByUsername(userLoginDTO.getUsername())).thenReturn(Optional.of(user));

        assertThrows(WrongAuthException.class, () -> authService.login(userLoginDTO));

        verify(userRepository, times(1)).findByUsername(userLoginDTO.getUsername());
    }

    @Test
    @DisplayName("Get user by username")
    void getUserByUsername() {
        User user = new User();
        user.setUsername("username");

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        User foundUser = authService.getUserByUsername(user.getUsername());

        assertEquals(user, foundUser);

        verify(userRepository, times(1)).findByUsername(user.getUsername());
    }

    @Test
    @DisplayName("Get user by username with wrong username")
    void getUserByUsernameWithWrongUsername() {
        User user = new User();
        user.setUsername("username");

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> authService.getUserByUsername(user.getUsername()));

        verify(userRepository, times(1)).findByUsername(user.getUsername());
    }

    @Test
    @DisplayName("Make user uploader successfully")
    void makeUploader() {
        User user = new User();
        user.setId(1);
        user.setRole("User");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        User updatedUser = authService.makeUploader(user.getId());

        assertEquals("UPLOADER", updatedUser.getRole());

        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    @DisplayName("Make user uploader with wrong id")
    void makeUploaderWithWrongId() {
        User user = new User();
        user.setId(1);
        user.setRole("USER");

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> authService.makeUploader(user.getId()));

        verify(userRepository, times(1)).findById(user.getId());
    }
}
