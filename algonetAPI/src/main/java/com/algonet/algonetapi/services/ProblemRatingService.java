package com.algonet.algonetapi.services;

import com.algonet.algonetapi.annotations.CheckOwn;
import com.algonet.algonetapi.models.dto.ProblemRatingDTOs.ProblemRatingUpdateDTO;
import com.algonet.algonetapi.models.entities.Problem;
import com.algonet.algonetapi.models.entities.ProblemRating;
import com.algonet.algonetapi.models.entities.ProblemRatingId;
import com.algonet.algonetapi.models.entities.Tag;
import com.algonet.algonetapi.repositories.ProblemRatingRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class ProblemRatingService {
    private final ProblemRatingRepository problemRatingRepository;
    private final EntityManager entityManager;

    @CheckOwn(entity = Problem.class)
    public ProblemRating update(Integer userId, Integer problemId, ProblemRatingUpdateDTO problemRatingUpdateDTO) {
        if(problemRatingUpdateDTO.getRating() == null || problemRatingUpdateDTO.getTagId() == null){
            throw new IllegalArgumentException();
        }
        ProblemRating problemRating = problemRatingRepository.findByProblemIdAndTagId(problemId, problemRatingUpdateDTO.getTagId())
                .orElse(new ProblemRating());
        problemRating.setProblem(entityManager.getReference(Problem.class, problemId));
        problemRating.setTag(entityManager.getReference(Tag.class, problemRatingUpdateDTO.getTagId()));
        problemRating.setRating(problemRatingUpdateDTO.getRating());
        ProblemRatingId problemRatingId = new ProblemRatingId(userId, problemRatingUpdateDTO.getTagId());
        problemRating.setId(problemRatingId);
        return problemRatingRepository.save(problemRating);
    }

    public List<ProblemRating> get(Integer id) {
        return problemRatingRepository.findAllByProblemId(id);
    }
}
