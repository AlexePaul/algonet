package com.algonet.algonetapi.services;

import com.algonet.algonetapi.models.dto.UserRatingDTOs.UserRatingUpdateDTO;
import com.algonet.algonetapi.models.entities.Tag;
import com.algonet.algonetapi.models.entities.User;
import com.algonet.algonetapi.models.entities.UserRating;
import com.algonet.algonetapi.models.entities.UserRatingId;
import com.algonet.algonetapi.repositories.UserRatingRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class UserRatingService {
    private final UserRatingRepository userRatingRepository;
    private final EntityManager entityManager;

    public UserRating update(User user, UserRatingUpdateDTO userRatingUpdateDTO) {
        UserRating userRating = userRatingRepository.findByUserAndTagId(user, userRatingUpdateDTO.getTagId())
                .orElse(new UserRating());
        userRating.setUser(user);
        userRating.setTag(entityManager.getReference(Tag.class, userRatingUpdateDTO.getTagId()));
        userRating.setRating(userRatingUpdateDTO.getRating());
        UserRatingId userRatingId = new UserRatingId(user.getId(), userRatingUpdateDTO.getTagId());
        userRating.setId(userRatingId);
        return userRatingRepository.save(userRating);
    }

    public List<UserRating> get(User user) {
        return userRatingRepository.findAllByUser(user);
    }
}
