package com.algonet.algonetapi.repositories;

import com.algonet.algonetapi.models.entities.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends JpaRepository<Problem, Integer> {

}
