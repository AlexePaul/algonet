package com.algonet.algonetapi.repositories;

import com.algonet.algonetapi.Models.entities.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

}
