package com.algonet.algonetapi.unit;

import com.algonet.algonetapi.models.dto.UserRatingDTOs.UserRatingUpdateDTO;
import com.algonet.algonetapi.models.entities.Tag;
import com.algonet.algonetapi.models.entities.User;
import com.algonet.algonetapi.models.entities.UserTagRating;
import com.algonet.algonetapi.repositories.UserTagRatingRepository;
import com.algonet.algonetapi.services.UserRatingService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRatingServiceTest {

    @Mock
    private UserTagRatingRepository userTagRatingRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private UserRatingService userRatingService;

    private User testUser;
    private Tag testTag;
    private UserTagRating testRating;
    private UserRatingUpdateDTO updateDTO;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("testuser");
        testUser.setTags(new ArrayList<>());

        testTag = new Tag();
        testTag.setId(1);
        testTag.setName("algorithms");

        testRating = new UserTagRating();
        testRating.setId(1);
        testRating.setUser(testUser);
        testRating.setTag(testTag);
        testRating.setRating(85);

        updateDTO = new UserRatingUpdateDTO();
        updateDTO.setTagId(1);
        updateDTO.setRating(90);
    }

    @Test
    void update_ShouldCreateNewRating_WhenRatingNotExists() {
        // Given
        when(entityManager.getReference(Tag.class, 1)).thenReturn(testTag);
        when(userTagRatingRepository.findByUserAndTag(testUser, testTag)).thenReturn(Optional.empty());
        when(userTagRatingRepository.save(any(UserTagRating.class))).thenReturn(testRating);

        // When
        UserTagRating result = userRatingService.update(testUser, updateDTO);

        // Then
        assertThat(result).isNotNull();
        verify(entityManager).getReference(Tag.class, 1);
        verify(userTagRatingRepository).findByUserAndTag(testUser, testTag);
        verify(userTagRatingRepository).save(any(UserTagRating.class));
        assertThat(testUser.getTags()).contains(testTag);
    }

    @Test
    void update_ShouldUpdateExistingRating_WhenRatingExists() {
        // Given
        when(entityManager.getReference(Tag.class, 1)).thenReturn(testTag);
        when(userTagRatingRepository.findByUserAndTag(testUser, testTag)).thenReturn(Optional.of(testRating));
        when(userTagRatingRepository.save(any(UserTagRating.class))).thenReturn(testRating);
        testUser.getTags().add(testTag); // Tag already exists

        // When
        UserTagRating result = userRatingService.update(testUser, updateDTO);

        // Then
        assertThat(result).isNotNull();
        verify(entityManager).getReference(Tag.class, 1);
        verify(userTagRatingRepository).findByUserAndTag(testUser, testTag);
        verify(userTagRatingRepository).save(testRating);
        assertThat(testUser.getTags()).hasSize(1);
    }

    @Test
    void update_ShouldAddTagToUser_WhenTagNotInUserTags() {
        // Given
        when(entityManager.getReference(Tag.class, 1)).thenReturn(testTag);
        when(userTagRatingRepository.findByUserAndTag(testUser, testTag)).thenReturn(Optional.of(testRating));
        when(userTagRatingRepository.save(any(UserTagRating.class))).thenReturn(testRating);

        // When
        userRatingService.update(testUser, updateDTO);

        // Then
        assertThat(testUser.getTags()).contains(testTag);
    }

    @Test
    void get_ShouldReturnAllUserRatings_WhenRatingsExist() {
        // Given
        List<UserTagRating> ratings = List.of(testRating);
        when(userTagRatingRepository.findAllByUser(testUser)).thenReturn(ratings);

        // When
        List<UserTagRating> result = userRatingService.get(testUser);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result).contains(testRating);
        verify(userTagRatingRepository).findAllByUser(testUser);
    }

    @Test
    void get_ShouldReturnEmptyList_WhenNoRatingsExist() {
        // Given
        when(userTagRatingRepository.findAllByUser(testUser)).thenReturn(List.of());

        // When
        List<UserTagRating> result = userRatingService.get(testUser);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(userTagRatingRepository).findAllByUser(testUser);
    }
}
