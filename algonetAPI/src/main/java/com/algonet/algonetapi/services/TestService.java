package com.algonet.algonetapi.services;

import com.algonet.algonetapi.exceptions.NotFoundException;
import com.algonet.algonetapi.models.dto.testDTOs.TestCreationDTO;
import com.algonet.algonetapi.models.entities.Problem;
import com.algonet.algonetapi.models.entities.Test;
import com.algonet.algonetapi.repositories.TestRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class TestService {

    private final TestRepository testRepository;
    private final EntityManager entityManager;

    public Test create(Integer problem_id, TestCreationDTO testCreationDTO) {
        Test test = new Test();

        // Use EntityManager.getReference() to get a reference to the Problem entity
        Problem problem = entityManager.getReference(Problem.class, problem_id);
        test.setProblem(problem);

        test.setInput(testCreationDTO.getInput());
        test.setOutput(testCreationDTO.getOutput());
        return testRepository.save(test);
    }

    public Test get(Integer id) {
        return testRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public Test update(Integer id, TestCreationDTO testCreationDTO) {
        Test test = testRepository.findById(id).orElseThrow(NotFoundException::new);
        test.setInput(testCreationDTO.getInput());
        test.setOutput(testCreationDTO.getOutput());
        return testRepository.save(test);
    }

    public void delete(Integer id) {
        testRepository.deleteById(id);
    }

    public List<Test> getAll(Integer problem_id) {
        return testRepository.findAllByProblem_id(problem_id);
    }
}
