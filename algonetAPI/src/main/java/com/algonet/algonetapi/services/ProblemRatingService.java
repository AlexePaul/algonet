package com.algonet.algonetapi.services;

import com.algonet.algonetapi.annotations.CheckOwn;
import com.algonet.algonetapi.models.dto.ProblemRatingDTOs.ProblemRatingUpdateDTO;
import com.algonet.algonetapi.models.entities.Problem;
import com.algonet.algonetapi.models.entities.ProblemTagRating;
import com.algonet.algonetapi.models.entities.Tag;
import com.algonet.algonetapi.repositories.ProblemTagRatingRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ProblemRatingService {
    private final ProblemTagRatingRepository problemTagRatingRepository;
    private final EntityManager entityManager;

    @CheckOwn(entity = Problem.class)
    public ProblemTagRating update(Integer userId, Integer problemId, ProblemRatingUpdateDTO problemRatingUpdateDTO) {
        if(problemRatingUpdateDTO.getRating() == null || problemRatingUpdateDTO.getTagId() == null){
            throw new IllegalArgumentException();
        }
        
        log.info("Updating problem tag rating for problem: {} and tag: {}", problemId, problemRatingUpdateDTO.getTagId());
        
        Problem problem = entityManager.getReference(Problem.class, problemId);
        Tag tag = entityManager.getReference(Tag.class, problemRatingUpdateDTO.getTagId());
        
        ProblemTagRating rating = problemTagRatingRepository.findByProblemAndTag(problem, tag)
                .orElse(new ProblemTagRating());
        
        rating.setProblem(problem);
        rating.setTag(tag);
        rating.setRating(problemRatingUpdateDTO.getRating());
        
        // Add tag to problem's tags if not already present
        if (!problem.getTags().contains(tag)) {
            problem.getTags().add(tag);
        }
        
        ProblemTagRating savedRating = problemTagRatingRepository.save(rating);
        log.info("Successfully updated problem tag rating with id: {}", savedRating.getId());
        return savedRating;
    }

    public List<ProblemTagRating> get(Integer problemId) {
        log.debug("Fetching all ratings for problem: {}", problemId);
        Problem problem = entityManager.getReference(Problem.class, problemId);
        return problemTagRatingRepository.findAllByProblem(problem);
    }
}
