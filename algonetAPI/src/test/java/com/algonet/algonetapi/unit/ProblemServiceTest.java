package com.algonet.algonetapi.unit;

import com.algonet.algonetapi.models.dto.problemDTOs.ProblemCreationDTO;
import com.algonet.algonetapi.models.dto.problemDTOs.ProblemPatchDTO;
import com.algonet.algonetapi.models.entities.Problem;
import com.algonet.algonetapi.models.entities.User;
import com.algonet.algonetapi.repositories.ProblemRepository;
import com.algonet.algonetapi.services.ProblemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProblemServiceTest {
    @Mock
    private ProblemRepository problemRepository;

    @InjectMocks
    private ProblemService problemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    @DisplayName("Create problem successfully")
    void createProblemSuccessfully() {
        User user = new User();
        ProblemCreationDTO problemCreationDTO = new ProblemCreationDTO("title", "description", "restrictions", 1, 2);
        Problem problem = new Problem();
        problem.setId(1);
        Instant fixedTime = Instant.parse("2025-01-04T10:00:00Z");

        BeanUtils.copyProperties(problemCreationDTO, problem);
        problem.setAuthor(user);
        problem.setCreatedAt(fixedTime);

        when(problemRepository.save(problem)).thenReturn(problem);
        when(problemRepository.save(any(Problem.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Problem createdProblem = problemService.create(user, problemCreationDTO, fixedTime);
        createdProblem.setId(1);

        assertEquals(problem, createdProblem);

        verify(problemRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Get problem successfully")
    void getProblemSuccessfully() {
        Problem problem = new Problem();
        problem.setId(1);
        when(problemRepository.findById(1)).thenReturn(Optional.of(problem));

        Problem gottenProblem = problemService.get(1);

        assertEquals(problem, gottenProblem);
        verify(problemRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Update problem successfully")
    void updateProblemSuccessfully() {
        Problem problem = new Problem();
        Problem updatedProblem = new Problem();
        ProblemPatchDTO problemPatchDTO = new ProblemPatchDTO("title", "description", "restrictions", 1, 2);
        BeanUtils.copyProperties(problemPatchDTO, updatedProblem);
        updatedProblem.setId(1);
        User user = new User();
        user.setId(1);
        problem.setAuthor(user);

        when(problemRepository.findById(1)).thenReturn(Optional.of(problem));
        when(problemRepository.save(any(Problem.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Problem gottenProblem = problemService.update(user.getId(), 1, problemPatchDTO);

        assertEquals(problem.getTitle(), gottenProblem.getTitle());
        assertEquals(problem.getBody(), gottenProblem.getBody());
        assertEquals(problem.getRestrictions(), gottenProblem.getRestrictions());
        assertEquals(problem.getTimeLimit(), gottenProblem.getTimeLimit());
        assertEquals(problem.getMemoryLimit(), gottenProblem.getMemoryLimit());
        assertEquals(problem.getAuthor(), gottenProblem.getAuthor());
        assertEquals(problem.getCreatedAt(), gottenProblem.getCreatedAt());
        verify(problemRepository, times(1)).findById(1);
        }

    @Test
    @DisplayName("Delete problem successfully")
    void deleteProblemSuccessfully() {
        Problem problem = new Problem();
        User user = new User();
        user.setId(1);
        problem.setAuthor(user);
        problem.setId(1);

        when(problemRepository.findById(1)).thenReturn(Optional.of(problem));

        problemService.delete(user.getId(), 1);

        verify(problemRepository, times(1)).deleteById(1);
    }

}
