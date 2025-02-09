package com.algonet.algonetapi.repositories;

import com.algonet.algonetapi.models.entities.ProblemRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProblemRatingRepository extends JpaRepository<ProblemRating, Integer> {
    @Query("""
            SELECT pr
            FROM ProblemRating pr
            WHERE pr.problem.id = :problemId and pr.tag.id = :tagId
            """)
    Optional<ProblemRating> findByProblemIdAndTagId(Integer problemId, Integer tagId);
    @Query("""
            SELECT pr
            FROM ProblemRating pr
            WHERE pr.problem.id = :problemId
            """)
    List<ProblemRating> findAllByProblemId(Integer problemId);
}
