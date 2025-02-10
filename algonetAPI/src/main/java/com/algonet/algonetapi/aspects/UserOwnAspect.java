package com.algonet.algonetapi.aspects;

import com.algonet.algonetapi.annotations.CheckOwn;
import com.algonet.algonetapi.models.entities.Problem;
import com.algonet.algonetapi.models.entities.Solution;
import com.algonet.algonetapi.repositories.ProblemRepository;
import com.algonet.algonetapi.repositories.SolutionRepository;
import com.algonet.algonetapi.repositories.TestRepository;
import lombok.AllArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Aspect
@AllArgsConstructor
@Component
public class UserOwnAspect {
    private final ProblemRepository problemRepository;
    private final SolutionRepository solutionRepository;
    private final TestRepository testRepository;

    @SuppressWarnings("ArgNamesWarningsInspection")
    @Before("@annotation(checkOwn) && args(authUserId, id, ..)")
    public void checkUserOwn(CheckOwn checkOwn, Integer authUserId, Integer id) {
        if (authUserId == null || id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Id");
        }

        Class<?> entityClass = checkOwn.entity();

        switch (entityClass) {
            case Class<?> cl when cl == Problem.class:
                checkProblem(authUserId, id);
                break;
            case Class<?> cl when cl == Solution.class:
                checkSolution(authUserId, id);
                break;
            case Class<?> cl when cl == TestRepository.class:
                checkTest(authUserId, id);
                break;
            default:
                throw new IllegalArgumentException("Unknown entity class: " + entityClass);
        }
    }

    private void checkProblem(Integer authUserId, Integer problemId) {
        if(!problemRepository.existsById(problemId)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid problemId");
        }
        //noinspection OptionalGetWithoutIsPresent
        if (!Objects.equals(problemRepository.findById(problemId).get().getAuthor().getId(), authUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the author of this problem");
        }
    }
    private void checkSolution(Integer authUserId, Integer solutionId) {
        if(!solutionRepository.existsById(solutionId)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid solutionId");
        }
        //noinspection OptionalGetWithoutIsPresent
        if (!Objects.equals(solutionRepository.findById(solutionId).get().getUser().getId(), authUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the author of this solution");
        }
    }
    private void checkTest(Integer authUserId, Integer testId) {
        if(!testRepository.existsById(testId)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid testId");
        }
        //noinspection OptionalGetWithoutIsPresent
        if (!Objects.equals(testRepository.findById(testId).get().getProblem().getAuthor().getId(), authUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the author of this test");
        }
    }
}
