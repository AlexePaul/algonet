package com.algonet.algonetapi.services;

import com.algonet.algonetapi.models.dto.testDTOs.TestCreationDTO;
import com.algonet.algonetapi.models.entities.Test;
import com.algonet.algonetapi.exceptions.NotFoundException;
import com.algonet.algonetapi.repositories.TestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TestService {
    private final TestRepository testRepository;
    public Test create(Integer problem_id, TestCreationDTO testCreationDTO) {
        Test test = new Test();
        test.setProblem_id(problem_id);
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
