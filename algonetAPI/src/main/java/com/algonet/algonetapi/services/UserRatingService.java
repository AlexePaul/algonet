package com.algonet.algonetapi.services;

import com.algonet.algonetapi.models.dto.UserRatingDTOs.UserRatingUpdateDTO;
import com.algonet.algonetapi.models.entities.Tag;
import com.algonet.algonetapi.models.entities.User;
import com.algonet.algonetapi.models.entities.UserTagRating;
import com.algonet.algonetapi.repositories.UserTagRatingRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class UserRatingService {
    private final UserTagRatingRepository userTagRatingRepository;
    private final EntityManager entityManager;

    public UserTagRating update(User user, UserRatingUpdateDTO userRatingUpdateDTO) {
        log.info("Updating user tag rating for user: {} and tag: {}", user.getUsername(), userRatingUpdateDTO.getTagId());
        
        Tag tag = entityManager.getReference(Tag.class, userRatingUpdateDTO.getTagId());
        
        UserTagRating rating = userTagRatingRepository.findByUserAndTag(user, tag)
                .orElse(new UserTagRating());
        
        rating.setUser(user);
        rating.setTag(tag);
        rating.setRating(userRatingUpdateDTO.getRating());
        
        // Add tag to user's tags if not already present
        if (!user.getTags().contains(tag)) {
            user.getTags().add(tag);
        }
        
        UserTagRating savedRating = userTagRatingRepository.save(rating);
        log.info("Successfully updated user tag rating with id: {}", savedRating.getId());
        return savedRating;
    }

    public List<UserTagRating> get(User user) {
        log.debug("Fetching all ratings for user: {}", user.getUsername());
        return userTagRatingRepository.findAllByUser(user);
    }
}
