package com.algonet.algonetapi.repositories;

import com.algonet.algonetapi.models.entities.User;
import com.algonet.algonetapi.models.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
    
    @Query("""
            SELECT up FROM UserProfile up WHERE up.user = :user
            """)
    Optional<UserProfile> findByUser(User user);
    
    @Query("""
            SELECT up FROM UserProfile up WHERE up.user.id = :userId
            """)
    Optional<UserProfile> findByUserId(Integer userId);
}
