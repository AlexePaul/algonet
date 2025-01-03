package com.algonet.algonetapi.services;

import com.algonet.algonetapi.exceptions.NotFoundException;
import com.algonet.algonetapi.models.dto.solutionDTOs.SolutionCreationDTO;
import com.algonet.algonetapi.models.entities.Solution;
import com.algonet.algonetapi.repositories.SolutionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;

@AllArgsConstructor
@Service
public class SolutionService {
    private final SolutionRepository solutionRepository;

    public Solution create(Integer user_id, Integer problem_id, SolutionCreationDTO solutionCreationDTO) {

        Solution solution = new Solution();
        BeanUtils.copyProperties(solutionCreationDTO, solution);
        solution.setProblem_id(problem_id);
        solution.setGrade(-1);
        solution.setCreated_at(Instant.now());
        solution.setUser_id(user_id);

        return solutionRepository.save(solution);
    }

    public Object get(Integer id) {
        return solutionRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public Object getByProblemIdAndUserId(Integer user_id, Integer problem_id) {
        return solutionRepository.findByProblemIdAndUserId(problem_id, user_id);
    }
}
