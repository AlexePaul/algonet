package com.algonet.algonetapi.services;

import com.algonet.algonetapi.exceptions.UnauthorizedException;
import com.algonet.algonetapi.models.dto.ProblemRatingDTOs.ProblemRatingUpdateDTO;
import com.algonet.algonetapi.models.entities.*;
import com.algonet.algonetapi.repositories.ProblemRatingRepository;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ProblemRatingService {
    private final ProblemRatingRepository problemRatingRepository;
    private final EntityManager entityManager;


    public ProblemRating update(User user, ProblemRatingUpdateDTO problemRatingUpdateDTO) {
        if(problemRatingUpdateDTO.getProblemId() == null || problemRatingUpdateDTO.getTagId() == null || problemRatingUpdateDTO.getRating() == null){
            throw new IllegalArgumentException("ProblemId, TagId and Rating must be provided");
        }
        if(!Objects.equals(entityManager.getReference(Problem.class, problemRatingUpdateDTO.getProblemId()).getAuthor().getId(), user.getId())){
            throw new UnauthorizedException();
        }
        ProblemRating problemRating = problemRatingRepository.findByProblemIdAndTagId(problemRatingUpdateDTO.getProblemId(), problemRatingUpdateDTO.getTagId())
                .orElse(new ProblemRating());
        problemRating.setProblem(entityManager.getReference(Problem.class, problemRatingUpdateDTO.getProblemId()));
        problemRating.setTag(entityManager.getReference(Tag.class, problemRatingUpdateDTO.getTagId()));
        problemRating.setRating(problemRatingUpdateDTO.getRating());
        ProblemRatingId problemRatingId = new ProblemRatingId(user.getId(), problemRatingUpdateDTO.getTagId());
        problemRating.setId(problemRatingId);
        return problemRatingRepository.save(problemRating);
    }

    public List<ProblemRating> get(Integer id) {
        return problemRatingRepository.findAllByProblemId(id);
    }
}
