package com.algonet.algonetapi.repositories;

import com.algonet.algonetapi.models.entities.User;
import com.algonet.algonetapi.models.entities.UserRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRatingRepository extends JpaRepository<UserRating, Integer> {
    @Query("""
            SELECT ur
            FROM UserRating ur
            WHERE ur.user = :user AND ur.tag.id = :tagId
            """)
    Optional<UserRating> findByUserAndTagId(User user, Integer tagId);

    @Query("""
            SELECT ur
            FROM UserRating ur
            WHERE ur.user = :user
            """)
    List<UserRating> findAllByUser(User user);
}
