package com.algonet.algonetapi.repositories;

import com.algonet.algonetapi.models.entities.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SolutionRepository extends JpaRepository<Solution, Integer> {
    @Query("""
            SELECT s
            FROM Solution s
            WHERE s.problem_id = :problemId
            """)
    Object findByProblemId(Integer problemId);
    @Query("""
            SELECT s
            FROM Solution s
            WHERE s.problem_id = :problemId AND s.user_id = :userId
            """)
    Object findByProblemIdAndUserId(Integer problemId, Integer userId);
}
