package com.algonet.algonetapi.unit;

import com.algonet.algonetapi.models.dto.solutionDTOs.SolutionCreationDTO;
import com.algonet.algonetapi.models.entities.Problem;
import com.algonet.algonetapi.models.entities.Solution;
import com.algonet.algonetapi.models.entities.User;
import com.algonet.algonetapi.repositories.SolutionRepository;
import com.algonet.algonetapi.services.QueueService;
import com.algonet.algonetapi.services.SolutionService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class SolutionServiceTest {

    @Mock
    private SolutionRepository solutionRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private QueueService queueService;

    @InjectMocks
    private SolutionService solutionService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    @DisplayName("Create solution successfully")
    void createSolutionSuccessfully() {
        User user = new User();
        Problem problem = new Problem();
        SolutionCreationDTO solutionCreationDTO = new SolutionCreationDTO("sourceCode");
        Solution solution = new Solution();
        Instant fixedTime = Instant.parse("2025-01-04T10:00:00Z");

        solution.setProblem(problem);
        solution.setUser(user);
        solution.setGrade(-1);
        solution.setCreatedAt(fixedTime);
        BeanUtils.copyProperties(solutionCreationDTO, solution);

        when(entityManager.find(Problem.class, problem.getId())).thenReturn(problem);
        when(solutionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(queueService).addSolutionToQueue(solution.getId());

        Solution createdSolution = solutionService.create(user, problem.getId(), solutionCreationDTO, fixedTime);

        assertEquals(solution, createdSolution);

    }

    @Test
    @DisplayName("Get solution successfully")
    void getSolutionSuccessfully() {
        Solution solution = new Solution();
        solution.setId(1);
        when(solutionRepository.findById(1)).thenReturn(java.util.Optional.of(solution));

        Solution gottenSolution = solutionService.get(1,1);

        assertEquals(solution, gottenSolution);
    }

    @Test
    @DisplayName("Get solution by user and problem id successfully")
    void getSolutionByUserAndProblemIdSuccessfully() {
        User user = new User();
        Problem problem = new Problem();
        problem.setId(1);
        when(solutionRepository.findByUserAndProblemId(user, problem.getId())).thenReturn(java.util.Optional.of(List.of(new Solution())));

        List<Solution> gottenSolutions = solutionService.getByUserAndProblemId(user, problem.getId());

        assertEquals(1, gottenSolutions.size());
    }
}
