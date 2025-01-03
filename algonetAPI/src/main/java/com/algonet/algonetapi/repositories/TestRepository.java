package com.algonet.algonetapi.repositories;

import com.algonet.algonetapi.models.entities.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TestRepository extends JpaRepository<Test, Integer> {
    @Query("""
            SELECT t
            FROM Test t
            WHERE t.problem_id = :problemId
            """)
    List<Test> findAllByProblem_id(Integer problemId);
}
