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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static com.algonet.algonetapi.utils.MapperUtils.copyNonNullProperties;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ProblemService {
    private final ProblemRepository problemRepository;
    
    public Problem create(User user, ProblemCreationDTO problemCreationDTO, Instant createdAt) {
        log.info("Creating new problem with title: {} for user: {}", problemCreationDTO.getTitle(), user.getUsername());
        Problem problem = new Problem();
        BeanUtils.copyProperties(problemCreationDTO, problem);
        problem.setAuthor(user);
        problem.setCreatedAt(createdAt);
        Problem savedProblem = problemRepository.save(problem);
        log.info("Successfully created problem with id: {}", savedProblem.getId());
        return savedProblem;
    }    public Problem get(Integer id) {
        log.debug("Fetching problem with id: {}", id);
        return problemRepository.findById(id).orElseThrow(NotFoundException::new);
    }
      public Problem getWithAuthor(Integer id) {
        log.debug("Fetching problem with author and details with id: {}", id);
        Problem problem = problemRepository.findByIdWithDetails(id);
        if (problem == null) {
            throw new NotFoundException();
        }
        return problem;
    }public Page<Problem> getAllPaginated(Pageable pageable) {
        log.debug("Fetching problems with pagination: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        return problemRepository.findAllSorted(pageable);
    }

    public Page<Problem> getAllPaginatedWithAuthor(Pageable pageable) {
        log.debug("Fetching problems with authors with pagination: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        return problemRepository.findAllSortedWithAuthor(pageable);
    }    public Page<Problem> searchByTitle(String title, Pageable pageable) {
        log.debug("Searching problems by title: '{}' with pagination", title);
        return problemRepository.findByTitleContaining(title, pageable);
    }

    public Page<Problem> searchByTitleWithAuthor(String title, Pageable pageable) {
        log.debug("Searching problems with authors by title: '{}' with pagination", title);
        return problemRepository.findByTitleContainingWithAuthor(title, pageable);
    }@CheckOwn(entity = Problem.class)
    public Problem update(Integer userId, Integer id, ProblemPatchDTO problemPatchDTO) {
        Problem problem = problemRepository.findById(id).orElseThrow(NotFoundException::new);
        copyNonNullProperties(problemPatchDTO, problem);
        return problemRepository.save(problem);
    }
    
    @CheckOwn(entity = Problem.class)
    public void delete(Integer userId, Integer id) {
        problemRepository.deleteById(id);
    }
}
