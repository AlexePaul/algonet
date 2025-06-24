package com.algonet.algonetapi.repositories;

import com.algonet.algonetapi.models.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    List<Tag> findByNameContainingIgnoreCase(String name);
}
