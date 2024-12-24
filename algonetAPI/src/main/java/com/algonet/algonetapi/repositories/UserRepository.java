package com.algonet.algonetapi.repositories;

import com.algonet.algonetapi.Models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("""
    SELECT u FROM User u where u.username = :username
""")
    Optional<User> findByUsername(String username);
}
