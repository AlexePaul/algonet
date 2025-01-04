package com.algonet.algonetapi.services;

import com.algonet.algonetapi.exceptions.NotFoundException;
import com.algonet.algonetapi.models.dto.solutionDTOs.SolutionCreationDTO;
import com.algonet.algonetapi.models.entities.Problem;
import com.algonet.algonetapi.models.entities.Solution;
import com.algonet.algonetapi.models.entities.User;
import com.algonet.algonetapi.repositories.SolutionRepository;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@Service
public class SolutionService {

    private final SolutionRepository solutionRepository;
    private final EntityManager entityManager;

    public Solution create(User user, Integer problem_id, SolutionCreationDTO solutionCreationDTO) {

        Solution solution = new Solution();
        BeanUtils.copyProperties(solutionCreationDTO, solution);

        Problem problem = entityManager.getReference(Problem.class, problem_id);
        solution.setProblem(problem);

        solution.setUser(user);

        solution.setGrade(-1);
        solution.setCreatedAt(Instant.now());

        return solutionRepository.save(solution);
    }

    public Solution get(Integer id) {
        return solutionRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public List<Solution> getByUserAndProblemId(User user, Integer problemId) {

        return solutionRepository.findByUserAndProblemId(user, problemId)
                .orElseThrow(NotFoundException::new);
    }
}
