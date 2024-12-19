package com.algonet.algonetapi.repositories;

import com.algonet.algonetapi.Models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
