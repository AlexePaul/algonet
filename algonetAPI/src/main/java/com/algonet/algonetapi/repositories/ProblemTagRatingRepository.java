package com.algonet.algonetapi.repositories;

import com.algonet.algonetapi.models.entities.Problem;
import com.algonet.algonetapi.models.entities.ProblemTagRating;
import com.algonet.algonetapi.models.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProblemTagRatingRepository extends JpaRepository<ProblemTagRating, Integer> {
    
    @Query("""
            SELECT ptr FROM ProblemTagRating ptr 
            WHERE ptr.problem = :problem AND ptr.tag = :tag
            """)
    Optional<ProblemTagRating> findByProblemAndTag(Problem problem, Tag tag);
    
    @Query("""
            SELECT ptr FROM ProblemTagRating ptr 
            WHERE ptr.problem = :problem
            """)
    List<ProblemTagRating> findAllByProblem(Problem problem);
    
    @Query("""
            SELECT ptr FROM ProblemTagRating ptr 
            WHERE ptr.tag = :tag
            """)
    List<ProblemTagRating> findAllByTag(Tag tag);
}
