package com.algonet.algonetapi.repositories;

import com.algonet.algonetapi.models.entities.Solution;
import com.algonet.algonetapi.models.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SolutionRepository extends JpaRepository<Solution, Integer> {
    @Query("""
            SELECT s
            FROM Solution s
            WHERE s.problem.id = :problemId AND s.user = :user
            """)
    Optional<List<Solution>> findByUserAndProblemId(User user, Integer problemId);
    
    @Query("""
            SELECT s FROM Solution s ORDER BY s.createdAt DESC
            """)
    Page<Solution> findAllSorted(Pageable pageable);
    
    @Query("""
            SELECT s FROM Solution s WHERE s.user = :user ORDER BY s.createdAt DESC
            """)
    Page<Solution> findByUserSorted(User user, Pageable pageable);
}
