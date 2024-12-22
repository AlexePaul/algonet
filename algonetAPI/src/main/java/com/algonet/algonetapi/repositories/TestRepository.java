package com.algonet.algonetapi.repositories;

import com.algonet.algonetapi.Models.entities.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long> {
}
