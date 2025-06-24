package com.algonet.algonetapi.repositories;

import com.algonet.algonetapi.models.entities.Tag;
import com.algonet.algonetapi.models.entities.User;
import com.algonet.algonetapi.models.entities.UserTagRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserTagRatingRepository extends JpaRepository<UserTagRating, Integer> {
    
    @Query("""
            SELECT utr FROM UserTagRating utr 
            WHERE utr.user = :user AND utr.tag = :tag
            """)
    Optional<UserTagRating> findByUserAndTag(User user, Tag tag);
    
    @Query("""
            SELECT utr FROM UserTagRating utr 
            WHERE utr.user = :user
            """)
    List<UserTagRating> findAllByUser(User user);
    
    @Query("""
            SELECT utr FROM UserTagRating utr 
            WHERE utr.tag = :tag
            """)
    List<UserTagRating> findAllByTag(Tag tag);
}
