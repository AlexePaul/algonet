package com.algonet.algonetapi.integration;

import com.algonet.algonetapi.config.TestConfig;
import com.algonet.algonetapi.models.entities.Tag;
import com.algonet.algonetapi.repositories.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
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
class TagControllerIT {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TagRepository tagRepository;

    private Tag testTag1;
    private Tag testTag2;

    @BeforeEach
    void setUp() {
        tagRepository.deleteAll();

        testTag1 = new Tag();
        testTag1.setName("algorithms");
        testTag1 = tagRepository.save(testTag1);

        testTag2 = new Tag();
        testTag2.setName("data-structures");
        testTag2 = tagRepository.save(testTag2);
    }

    @Test
    @WithMockUser(username = "testuser")
    void getAllTags_ShouldReturnAllTags_WhenTagsExist() throws Exception {
        mockMvc.perform(get("/api/v1/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].name", hasItems("algorithms", "data-structures")));
    }

    @Test
    @WithMockUser(username = "testuser")
    void getTagById_ShouldReturnTag_WhenTagExists() throws Exception {
        mockMvc.perform(get("/api/v1/tags/{id}", testTag1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("algorithms")))
                .andExpect(jsonPath("$.id", is(testTag1.getId())));
    }

    @Test
    @WithMockUser(username = "testuser")
    void getTagById_ShouldReturnNotFound_WhenTagNotExists() throws Exception {
        mockMvc.perform(get("/api/v1/tags/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testuser")
    void searchTags_ShouldReturnFilteredTags_WhenSearchTermProvided() throws Exception {
        mockMvc.perform(get("/api/v1/tags")
                .param("name", "algorithm"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("algorithms")));
    }

    @Test
    @WithMockUser(username = "testuser")
    void searchTags_ShouldReturnEmptyList_WhenNoMatches() throws Exception {
        mockMvc.perform(get("/api/v1/tags")
                .param("name", "nonexistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getAllTags_ShouldReturnUnauthorized_WhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/tags"))
                .andExpect(status().isUnauthorized());
    }
}
