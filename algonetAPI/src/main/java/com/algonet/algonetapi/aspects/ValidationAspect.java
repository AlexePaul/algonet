package com.algonet.algonetapi.aspects;

import com.algonet.algonetapi.repositories.ProblemRepository;
import lombok.AllArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Aspect
@Component
@AllArgsConstructor
public class ValidationAspect {
    private final ProblemRepository problemRepository;
    @Before("@annotation(com.algonet.algonetapi.annotations.ValidateProblemId) && args(problem_id)")
    public void validateProblemId(Integer problem_id) {
        if (problem_id == null || !problemRepository.existsById(problem_id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid problem_id");
        }
    }
}
