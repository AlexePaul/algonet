package com.algonet.algonetapi.unit;

import com.algonet.algonetapi.models.dto.ProblemRatingDTOs.ProblemRatingUpdateDTO;
import com.algonet.algonetapi.models.entities.*;
import com.algonet.algonetapi.repositories.ProblemRatingRepository;
import com.algonet.algonetapi.services.ProblemRatingService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProblemRatingServiceTest {
    @Mock
    private ProblemRatingRepository problemRatingRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ProblemRatingService problemRatingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    @DisplayName("Adding a rating to a problem successfully")
    void addRatingToProblem() {
        User user = new User();
        user.setId(1);

        ProblemRatingUpdateDTO problemRatingUpdateDTO = new ProblemRatingUpdateDTO(3,5);

        Problem problem = new Problem();
        problem.setId(2);
        problem.setAuthor(user);

        Tag tag = new Tag();
        tag.setId(3);

        ProblemRating problemRating = new ProblemRating();
        problemRating.setProblem(problem);
        problemRating.setTag(tag);
        problemRating.setRating(5);
        ProblemRatingId problemRatingId = new ProblemRatingId(user.getId(), problemRatingUpdateDTO.getTagId());
        problemRating.setId(problemRatingId);

        when(entityManager.getReference(Problem.class,2)).thenReturn(problem);
        when(entityManager.getReference(Tag.class,problemRatingUpdateDTO.getTagId())).thenReturn(tag);
        when(problemRatingRepository.findByProblemIdAndTagId(2, problemRatingUpdateDTO.getTagId())).thenReturn(Optional.empty());
        when(problemRatingRepository.save(any(ProblemRating.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ProblemRating newProblemRating = problemRatingService.update(user.getId(),2, problemRatingUpdateDTO);

        assertEquals(problemRating, newProblemRating);

        verify(problemRatingRepository, times(1)).save(any());
        verify(problemRatingRepository, times(1)).findByProblemIdAndTagId(any(), any());
        verify(entityManager, times(2)).getReference(any(), any());
    }
    @Test
    @DisplayName("Updating a rating to a problem successfully")
    void updateRatingToProblem() {
        User user = new User();
        user.setId(1);

        ProblemRatingUpdateDTO problemRatingUpdateDTO = new ProblemRatingUpdateDTO(3,5);

        Problem problem = new Problem();
        problem.setId(2);
        problem.setAuthor(user);

        Tag tag = new Tag();
        tag.setId(3);

        ProblemRating problemRating = new ProblemRating();
        problemRating.setProblem(problem);
        problemRating.setTag(tag);
        problemRating.setRating(4);
        ProblemRatingId problemRatingId = new ProblemRatingId(user.getId(), problemRatingUpdateDTO.getTagId());
        problemRating.setId(problemRatingId);

        when(entityManager.getReference(Problem.class,2)).thenReturn(problem);
        when(entityManager.getReference(Tag.class,problemRatingUpdateDTO.getTagId())).thenReturn(tag);
        when(problemRatingRepository.findByProblemIdAndTagId(2, problemRatingUpdateDTO.getTagId())).thenReturn(Optional.of(problemRating));
        when(problemRatingRepository.save(any(ProblemRating.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ProblemRating newProblemRating = problemRatingService.update(user.getId(),2, problemRatingUpdateDTO);

        assertEquals(problemRating, newProblemRating);
        assertEquals(5, newProblemRating.getRating());


        verify(problemRatingRepository, times(1)).save(any());
        verify(problemRatingRepository, times(1)).findByProblemIdAndTagId(any(), any());
        verify(entityManager, times(2)).getReference(any(), any());
    }

    @Test
    @DisplayName("Updating a rating to a problem unsuccessfully, missing fields")
    void addRatingToProblemMissingFields() {
        User user = new User();
        user.setId(1);

        // tagId is null
        ProblemRatingUpdateDTO problemRatingUpdateDTOTID = new ProblemRatingUpdateDTO(null,5);
        // rating is null
        ProblemRatingUpdateDTO problemRatingUpdateDTOR = new ProblemRatingUpdateDTO(3,null);

        assertThrows(IllegalArgumentException.class, () -> problemRatingService.update(user.getId(),2, problemRatingUpdateDTOTID));
        assertThrows(IllegalArgumentException.class, () -> problemRatingService.update(user.getId(),2, problemRatingUpdateDTOR));

        verify(problemRatingRepository, times(0)).save(any());
        verify(problemRatingRepository, times(0)).findByProblemIdAndTagId(any(), any());
        verify(entityManager, times(0)).getReference(any(), any());
    }

    @Test
    @DisplayName("Getting all ratings of a problem")
    void getRatingsOfProblem() {
        ProblemRating problemRating = new ProblemRating();
        problemRating.setRating(5);

        when(problemRatingRepository.findAllByProblemId(1)).thenReturn(java.util.List.of(problemRating));

        assertEquals(1, problemRatingService.get(1).size());
        assertEquals(5, problemRatingService.get(1).getFirst().getRating());

        verify(problemRatingRepository, times(2)).findAllByProblemId(1);
    }
}
