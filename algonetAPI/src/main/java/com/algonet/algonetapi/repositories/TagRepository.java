package com.algonet.algonetapi.repositories;

import com.algonet.algonetapi.Models.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Integer> {
}
