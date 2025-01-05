package com.algonet.algonetapi.aspects;

import com.algonet.algonetapi.repositories.ProblemRepository;
import lombok.AllArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Aspect
@AllArgsConstructor
@Component
public class ProblemAuthorAspect {
    private final ProblemRepository problemRepository;
    @Before("@annotation(com.algonet.algonetapi.annotations.CheckProblemAuthor) && args (authUser, .., problem_id)")
    public void checkProblemAuthor(Integer problem_id, Integer authUser) {
        if(problem_id == null || !problemRepository.existsById(problem_id)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid problem_id");
        }
        if (problemRepository.findById(problem_id).get().getAuthor().getId() != authUser) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the author of this problem");
        }
    }
}
