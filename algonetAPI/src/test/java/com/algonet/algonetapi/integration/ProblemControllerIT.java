package com.algonet.algonetapi.integration;

import com.algonet.algonetapi.config.TestConfig;
import com.algonet.algonetapi.models.dto.problemDTOs.ProblemCreationDTO;
import com.algonet.algonetapi.models.entities.Problem;
import com.algonet.algonetapi.models.entities.User;
import com.algonet.algonetapi.repositories.ProblemRepository;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@Transactional
@Import(TestConfig.class)
class ProblemControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private UserRepository userRepository;    private User testUser;
    private Problem testProblem;

    @BeforeEach
    void setUp() {
        problemRepository.deleteAll();
        userRepository.deleteAll();
        
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setCreatedAt(java.time.Instant.now());
        testUser.setRole("USER");
        testUser = userRepository.save(testUser);

        testProblem = new Problem();
        testProblem.setTitle("Test Problem");
        testProblem.setBody("Test body content");
        testProblem.setRestrictions("Test restrictions");
        testProblem.setTimeLimit(1000);
        testProblem.setMemoryLimit(256);
        testProblem.setAuthor(testUser);
        testProblem = problemRepository.save(testProblem);
    }

    private void setUpAuthentication() {
        Authentication auth = new UsernamePasswordAuthenticationToken(
            testUser, 
            null, 
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(auth);
        SecurityContextHolder.setContext(securityContext);
    }    @Test
    @WithMockUser(username = "testuser")
    void getAllProblems_ShouldReturnProblems_WhenProblemsExist() throws Exception {
        mockMvc.perform(get("/api/v1/problems"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.content[0].title", is("Test Problem")))
                .andExpect(jsonPath("$.content[0].body", is("Test body content")));
    }

    @Test
    @WithMockUser(username = "testuser")
    void getProblemById_ShouldReturnProblem_WhenProblemExists() throws Exception {
        mockMvc.perform(get("/api/v1/problems/{id}", testProblem.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Test Problem")))
                .andExpect(jsonPath("$.body", is("Test body content")))
                .andExpect(jsonPath("$.timeLimit", is(1000)));
    }

    @Test
    @WithMockUser(username = "testuser")
    void getProblemById_ShouldReturnNotFound_WhenProblemNotExists() throws Exception {
        mockMvc.perform(get("/api/v1/problems/{id}", 999))
                .andExpect(status().isNotFound());
    }    @Test
    void createProblem_ShouldCreateProblem_WhenValidData() throws Exception {
        ProblemCreationDTO dto = new ProblemCreationDTO();
        dto.setTitle("New Problem");
        dto.setBody("New body content");
        dto.setRestrictions("New restrictions");
        dto.setTimeLimit(2000);
        dto.setMemoryLimit(512);

        mockMvc.perform(post("/api/v1/problems")
                .with(SecurityMockMvcRequestPostProcessors.authentication(
                    new UsernamePasswordAuthenticationToken(
                        testUser, 
                        null, 
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                    )
                ))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("New Problem")))
                .andExpect(jsonPath("$.body", is("New body content")))
                .andExpect(jsonPath("$.timeLimit", is(2000)));
    }

    @Test
    void createProblem_ShouldReturnUnauthorized_WhenNotAuthenticated() throws Exception {
        ProblemCreationDTO dto = new ProblemCreationDTO();
        dto.setTitle("New Problem");
        dto.setBody("New body content");
        dto.setRestrictions("New restrictions");
        dto.setTimeLimit(2000);
        dto.setMemoryLimit(512);

        mockMvc.perform(post("/api/v1/problems")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }    @Test
    @WithMockUser(username = "testuser")
    void searchProblems_ShouldReturnFilteredProblems_WhenSearchTermProvided() throws Exception {
        mockMvc.perform(get("/api/v1/problems")
                .param("title", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.content[0].title", containsString("Test")));
    }

    @Test
    @WithMockUser(username = "testuser")
    void searchProblems_ShouldReturnEmptyList_WhenNoMatches() throws Exception {
        mockMvc.perform(get("/api/v1/problems")
                .param("title", "NonExistentProblem"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)));
    }
}
