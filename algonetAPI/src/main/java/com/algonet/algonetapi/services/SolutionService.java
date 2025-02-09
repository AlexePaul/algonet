package com.algonet.algonetapi.services;

import com.algonet.algonetapi.annotations.CheckOwn;
import com.algonet.algonetapi.exceptions.NotFoundException;
import com.algonet.algonetapi.models.dto.solutionDTOs.SolutionCreationDTO;
import com.algonet.algonetapi.models.entities.Problem;
import com.algonet.algonetapi.models.entities.Solution;
import com.algonet.algonetapi.models.entities.User;
import com.algonet.algonetapi.repositories.SolutionRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class SolutionService {

    private final SolutionRepository solutionRepository;
    private final EntityManager entityManager;
    private final QueueService queueService;

    public Solution create(User user, Integer problem_id, SolutionCreationDTO solutionCreationDTO, Instant now) {

        Solution solution = new Solution();
        BeanUtils.copyProperties(solutionCreationDTO, solution);

        Problem problem = entityManager.find(Problem.class, problem_id);
        solution.setProblem(problem);

        solution.setUser(user);

        solution.setGrade(-1);
        solution.setCreatedAt(now);

        //solution has to be saved before adding to queue,
        // so that the grader doesn't try to grade a solution that doesn't exist
        var savedSolution = solutionRepository.save(solution);

        queueService.addSolutionToQueue(solution.getId());

        return savedSolution;
    }
    @CheckOwn(entity = Solution.class)
    public Solution get(@SuppressWarnings("unused") Integer userId, Integer id) {
        return solutionRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public List<Solution> getByUserAndProblemId(User user, Integer problemId) {

        return solutionRepository.findByUserAndProblemId(user, problemId)
                .orElseThrow(NotFoundException::new);
    }


}
