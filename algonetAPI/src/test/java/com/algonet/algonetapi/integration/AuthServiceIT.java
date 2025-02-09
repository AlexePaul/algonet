package com.algonet.algonetapi.integration;

import com.algonet.algonetapi.models.dto.userDTOs.UserCreationDTO;
import com.algonet.algonetapi.models.dto.userDTOs.UserLoginDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthServiceIT {

    @Autowired
    private MockMvc mockMvc; // Automatically configured by @AutoConfigureMockMvc

    @Autowired
    private ObjectMapper objectMapper; // Automatically configured by Spring Boot

    @Test
    @DisplayName("Should register a user")
    void registerUser() throws Exception {
        // Arrange
        UserCreationDTO userCreationDTO = new UserCreationDTO("test", "test", "test@test.com");
        String jsonRequest = objectMapper.writeValueAsString(userCreationDTO);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should not register a user with an existing username")
    void registerUserWithExistingUsername() throws Exception {
        // Arrange
        UserCreationDTO userCreationDTO = new UserCreationDTO("test", "test", "test@test.com");
        String jsonRequest = objectMapper.writeValueAsString(userCreationDTO);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Should not register a user with an existing email")
    void registerUserWithExistingEmail() throws Exception {
        // Arrange
        UserCreationDTO userCreationDTO = new UserCreationDTO("test2", "test", "test@test.com");
        String jsonRequest = objectMapper.writeValueAsString(userCreationDTO);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Should Authenticate a user")
    void authenticateUser() throws Exception {
        // Arrange
        UserLoginDTO userLoginDTO = new UserLoginDTO("test", "test");
        String jsonRequest = objectMapper.writeValueAsString(userLoginDTO);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should not Authenticate a user with wrong credentials")
    void authenticateUserWithWrongCredentials() throws Exception {
        // Arrange
        UserLoginDTO userLoginDTO = new UserLoginDTO("test", "wrongpassword");
        String jsonRequest = objectMapper.writeValueAsString(userLoginDTO);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Should not Authenticate a user with wrong username")
    void authenticateUserWithWrongUsername() throws Exception {
        // Arrange
        UserLoginDTO userLoginDTO = new UserLoginDTO("wrongusername", "test");
        String jsonRequest = objectMapper.writeValueAsString(userLoginDTO);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().is4xxClientError());
    }
}
