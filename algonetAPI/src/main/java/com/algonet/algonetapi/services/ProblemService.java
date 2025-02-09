package com.algonet.algonetapi.services;

import com.algonet.algonetapi.annotations.CheckOwn;
import com.algonet.algonetapi.exceptions.NotFoundException;
import com.algonet.algonetapi.models.dto.problemDTOs.ProblemCreationDTO;
import com.algonet.algonetapi.models.dto.problemDTOs.ProblemPatchDTO;
import com.algonet.algonetapi.models.entities.Problem;
import com.algonet.algonetapi.models.entities.User;
import com.algonet.algonetapi.repositories.ProblemRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static com.algonet.algonetapi.utils.MapperUtils.copyNonNullProperties;

@Service
@AllArgsConstructor
@Transactional
public class ProblemService {
    private final ProblemRepository problemRepository;
    public Problem create(User user, ProblemCreationDTO problemCreationDTO, Instant createdAt) {
        Problem problem = new Problem();
        BeanUtils.copyProperties(problemCreationDTO, problem);
        problem.setAuthor(user);
        problem.setCreatedAt(createdAt);
        return problemRepository.save(problem);
    }

    public Problem get(Integer id) {
        return problemRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @CheckOwn(entity = Problem.class)
    public Problem update(@SuppressWarnings("unused") Integer userId, Integer id, ProblemPatchDTO problemPatchDTO) {
        Problem problem = problemRepository.findById(id).orElseThrow(NotFoundException::new);
        copyNonNullProperties(problemPatchDTO, problem);
        return problemRepository.save(problem);
    }
    @CheckOwn(entity = Problem.class)
    public void delete(@SuppressWarnings("unused") Integer userId, Integer id) {
        problemRepository.deleteById(id);
    }
}
