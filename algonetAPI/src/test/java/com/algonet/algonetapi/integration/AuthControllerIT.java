package com.algonet.algonetapi.integration;

import com.algonet.algonetapi.config.TestConfig;
import com.algonet.algonetapi.models.dto.userDTOs.UserCreationDTO;
import com.algonet.algonetapi.models.dto.userDTOs.UserLoginDTO;
import com.algonet.algonetapi.models.entities.User;
import com.algonet.algonetapi.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@Transactional
@Import(TestConfig.class)
class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserCreationDTO validUserCreationDTO;
    private UserLoginDTO validUserLoginDTO;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        validUserCreationDTO = new UserCreationDTO();
        validUserCreationDTO.setUsername("testuser");
        validUserCreationDTO.setEmail("test@example.com");
        validUserCreationDTO.setPassword("password123");

        validUserLoginDTO = new UserLoginDTO();
        validUserLoginDTO.setUsername("testuser");
        validUserLoginDTO.setPassword("password123");
    }

    @Test
    void register_ShouldCreateUser_WhenValidData() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUserCreationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.email", is("test@example.com")))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    void register_ShouldReturnBadRequest_WhenInvalidData() throws Exception {
        UserCreationDTO invalidDTO = new UserCreationDTO();
        invalidDTO.setUsername(""); // Invalid username
        invalidDTO.setEmail("invalid-email"); // Invalid email
        invalidDTO.setPassword("123"); // Too short password

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }    @Test
    void login_ShouldReturnToken_WhenValidCredentials() throws Exception {
        // First create a user
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setCreatedAt(java.time.Instant.now());
        user.setRole("USER");
        userRepository.save(user);

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUserLoginDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(not(emptyString())));
    }

    @Test
    void login_ShouldReturnUnauthorized_WhenInvalidCredentials() throws Exception {
        UserLoginDTO invalidLoginDTO = new UserLoginDTO();
        invalidLoginDTO.setUsername("nonexistent");
        invalidLoginDTO.setPassword("wrongpassword");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidLoginDTO)))
                .andExpect(status().isUnauthorized());
    }    @Test
    void getUser_ShouldReturnUserInfo_WhenAuthenticated() throws Exception {
        // First create and save a user
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setCreatedAt(java.time.Instant.now());
        user.setRole("USER");
        userRepository.save(user);

        // Login to get token
        String response = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUserLoginDTO)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = response; // Assuming the response is just the token

        mockMvc.perform(get("/api/v1/auth")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.email", is("test@example.com")));
    }

    @Test
    void getUser_ShouldReturnUnauthorized_WhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/auth"))
                .andExpect(status().isUnauthorized());
    }
}
