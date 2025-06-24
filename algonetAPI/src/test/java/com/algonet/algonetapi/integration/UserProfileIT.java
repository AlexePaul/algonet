package com.algonet.algonetapi.integration;

import com.algonet.algonetapi.models.dto.userProfileDTOs.UserProfileCreationDTO;
import com.algonet.algonetapi.models.entities.User;
import com.algonet.algonetapi.models.entities.UserProfile;
import com.algonet.algonetapi.repositories.UserProfileRepository;
import com.algonet.algonetapi.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UserProfileIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setRole("USER");
        testUser.setCreatedAt(Instant.now());
        testUser = userRepository.save(testUser);
        
        // Set up authentication with our User entity as principal
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(testUser, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }    @Test
    void shouldCreateUserProfile() throws Exception {
        UserProfileCreationDTO dto = new UserProfileCreationDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setBio("Software Engineer");
        dto.setLocation("New York");

        mockMvc.perform(post("/api/v1/user-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }    @Test
    void shouldGetUserProfile() throws Exception {
        // Create a profile first
        UserProfile profile = new UserProfile();
        profile.setUser(testUser);
        profile.setFirstName("Jane");
        profile.setLastName("Smith");
        userProfileRepository.save(profile);

        mockMvc.perform(get("/api/v1/user-profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }
}
