package com.algonet.algonetapi.services;

import com.algonet.algonetapi.Models.dto.ProblemCreationDTO;
import com.algonet.algonetapi.Models.dto.TestCreationDTO;
import com.algonet.algonetapi.Models.entities.Problem;
import com.algonet.algonetapi.Models.entities.Test;
import com.algonet.algonetapi.repositories.ProblemRepository;
import com.algonet.algonetapi.repositories.TestRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class ProblemService {
    private ProblemRepository problemRepository;
    private TestRepository testRepository;
    @Transactional
    public Problem create(ProblemCreationDTO problemCreationDTO, List<TestCreationDTO> testCreationDTOS) {
        Problem newProblem = new Problem();

        BeanUtils.copyProperties(problemCreationDTO, newProblem);
        newProblem.setCreated_at(Instant.now());

        Problem savedProblem = problemRepository.save(newProblem);

        List<Test> newTests = testCreationDTOS.stream().map(
                dto -> {
                    Test newTest = new Test();
                    BeanUtils.copyProperties(dto, newTest);
                    newTest.setProblem_id(savedProblem.getId());
                    return newTest;
                }
        ).toList();
        testRepository.saveAll(newTests);

        return savedProblem;
    }
}
