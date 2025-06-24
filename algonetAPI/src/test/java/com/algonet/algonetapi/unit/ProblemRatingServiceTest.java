package com.algonet.algonetapi.unit;

import com.algonet.algonetapi.models.dto.ProblemRatingDTOs.ProblemRatingUpdateDTO;
import com.algonet.algonetapi.models.entities.Problem;
import com.algonet.algonetapi.models.entities.ProblemTagRating;
import com.algonet.algonetapi.models.entities.Tag;
import com.algonet.algonetapi.repositories.ProblemTagRatingRepository;
import com.algonet.algonetapi.services.ProblemRatingService;
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
class ProblemRatingServiceTest {

    @Mock
    private ProblemTagRatingRepository problemTagRatingRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ProblemRatingService problemRatingService;

    private Problem testProblem;
    private Tag testTag;
    private ProblemTagRating testRating;
    private ProblemRatingUpdateDTO updateDTO;

    @BeforeEach
    void setUp() {
        testProblem = new Problem();
        testProblem.setId(1);
        testProblem.setTitle("Test Problem");
        testProblem.setTags(new ArrayList<>());

        testTag = new Tag();
        testTag.setId(1);
        testTag.setName("algorithms");

        testRating = new ProblemTagRating();
        testRating.setId(1);
        testRating.setProblem(testProblem);
        testRating.setTag(testTag);
        testRating.setRating(85);

        updateDTO = new ProblemRatingUpdateDTO();
        updateDTO.setTagId(1);
        updateDTO.setRating(90);
    }

    @Test
    void update_ShouldCreateNewRating_WhenRatingNotExists() {
        // Given
        when(entityManager.getReference(Problem.class, 1)).thenReturn(testProblem);
        when(entityManager.getReference(Tag.class, 1)).thenReturn(testTag);
        when(problemTagRatingRepository.findByProblemAndTag(testProblem, testTag)).thenReturn(Optional.empty());
        when(problemTagRatingRepository.save(any(ProblemTagRating.class))).thenReturn(testRating);

        // When
        ProblemTagRating result = problemRatingService.update(1, 1, updateDTO);

        // Then
        assertThat(result).isNotNull();
        verify(entityManager).getReference(Problem.class, 1);
        verify(entityManager).getReference(Tag.class, 1);
        verify(problemTagRatingRepository).findByProblemAndTag(testProblem, testTag);
        verify(problemTagRatingRepository).save(any(ProblemTagRating.class));
        assertThat(testProblem.getTags()).contains(testTag);
    }

    @Test
    void update_ShouldUpdateExistingRating_WhenRatingExists() {
        // Given
        when(entityManager.getReference(Problem.class, 1)).thenReturn(testProblem);
        when(entityManager.getReference(Tag.class, 1)).thenReturn(testTag);
        when(problemTagRatingRepository.findByProblemAndTag(testProblem, testTag)).thenReturn(Optional.of(testRating));
        when(problemTagRatingRepository.save(any(ProblemTagRating.class))).thenReturn(testRating);
        testProblem.getTags().add(testTag); // Tag already exists

        // When
        ProblemTagRating result = problemRatingService.update(1, 1, updateDTO);

        // Then
        assertThat(result).isNotNull();
        verify(entityManager).getReference(Problem.class, 1);
        verify(entityManager).getReference(Tag.class, 1);
        verify(problemTagRatingRepository).findByProblemAndTag(testProblem, testTag);
        verify(problemTagRatingRepository).save(testRating);
        assertThat(testProblem.getTags()).hasSize(1);
    }

    @Test
    void update_ShouldAddTagToProblem_WhenTagNotInProblemTags() {
        // Given
        when(entityManager.getReference(Problem.class, 1)).thenReturn(testProblem);
        when(entityManager.getReference(Tag.class, 1)).thenReturn(testTag);
        when(problemTagRatingRepository.findByProblemAndTag(testProblem, testTag)).thenReturn(Optional.of(testRating));
        when(problemTagRatingRepository.save(any(ProblemTagRating.class))).thenReturn(testRating);

        // When
        problemRatingService.update(1, 1, updateDTO);

        // Then
        assertThat(testProblem.getTags()).contains(testTag);
    }

    @Test
    void update_ShouldThrowException_WhenRatingIsNull() {
        // Given
        updateDTO.setRating(null);

        // When & Then
        assertThatThrownBy(() -> problemRatingService.update(1, 1, updateDTO))
                .isInstanceOf(IllegalArgumentException.class);

        verify(entityManager, never()).getReference(any(), any());
        verify(problemTagRatingRepository, never()).save(any());
    }

    @Test
    void update_ShouldThrowException_WhenTagIdIsNull() {
        // Given
        updateDTO.setTagId(null);

        // When & Then
        assertThatThrownBy(() -> problemRatingService.update(1, 1, updateDTO))
                .isInstanceOf(IllegalArgumentException.class);

        verify(entityManager, never()).getReference(any(), any());
        verify(problemTagRatingRepository, never()).save(any());
    }

    @Test
    void get_ShouldReturnAllProblemRatings_WhenRatingsExist() {
        // Given
        List<ProblemTagRating> ratings = List.of(testRating);
        when(entityManager.getReference(Problem.class, 1)).thenReturn(testProblem);
        when(problemTagRatingRepository.findAllByProblem(testProblem)).thenReturn(ratings);

        // When
        List<ProblemTagRating> result = problemRatingService.get(1);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result).contains(testRating);
        verify(entityManager).getReference(Problem.class, 1);
        verify(problemTagRatingRepository).findAllByProblem(testProblem);
    }

    @Test
    void get_ShouldReturnEmptyList_WhenNoRatingsExist() {
        // Given
        when(entityManager.getReference(Problem.class, 1)).thenReturn(testProblem);
        when(problemTagRatingRepository.findAllByProblem(testProblem)).thenReturn(List.of());

        // When
        List<ProblemTagRating> result = problemRatingService.get(1);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(entityManager).getReference(Problem.class, 1);
        verify(problemTagRatingRepository).findAllByProblem(testProblem);
    }
}
