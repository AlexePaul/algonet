package com.algonet.algonetapi.unit;

import com.algonet.algonetapi.models.dto.UserRatingDTOs.UserRatingUpdateDTO;
import com.algonet.algonetapi.models.entities.Tag;
import com.algonet.algonetapi.models.entities.User;
import com.algonet.algonetapi.models.entities.UserRating;
import com.algonet.algonetapi.models.entities.UserRatingId;
import com.algonet.algonetapi.repositories.UserRatingRepository;
import com.algonet.algonetapi.services.UserRatingService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserRatingServiceTest {

    @Mock
    private UserRatingRepository userRatingRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private UserRatingService userRatingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Update user rating successfully")
    void updateUserRatingSuccessfully() {
        User user = new User();
        user.setId(1);

        Tag tag = new Tag();
        tag.setId(2);

        UserRatingUpdateDTO userRatingUpdateDTO = new UserRatingUpdateDTO(2, 5);

        UserRating existingUserRating = new UserRating();
        existingUserRating.setUser(user);
        existingUserRating.setTag(tag);
        existingUserRating.setRating(3);
        UserRatingId existingUserRatingId = new UserRatingId(user.getId(), tag.getId());
        existingUserRating.setId(existingUserRatingId);

        when(userRatingRepository.findByUserAndTagId(user, userRatingUpdateDTO.getTagId())).thenReturn(Optional.of(existingUserRating));
        when(entityManager.getReference(Tag.class, userRatingUpdateDTO.getTagId())).thenReturn(tag);
        when(userRatingRepository.save(any(UserRating.class))).thenReturn(existingUserRating);

        UserRating updatedUserRating = userRatingService.update(user, userRatingUpdateDTO);

        assertEquals(existingUserRating.getUser(), updatedUserRating.getUser());
        assertEquals(existingUserRating.getTag(), updatedUserRating.getTag());
        assertEquals(userRatingUpdateDTO.getRating(), updatedUserRating.getRating());
        assertEquals(existingUserRating.getId(), updatedUserRating.getId());

        verify(userRatingRepository, times(1)).findByUserAndTagId(user, userRatingUpdateDTO.getTagId());
        verify(userRatingRepository, times(1)).save(any(UserRating.class));
    }

    @Test
    @DisplayName("Create user rating when not exists")
    void createUserRatingWhenNotExists() {
        User user = new User();
        user.setId(1);

        Tag tag = new Tag();
        tag.setId(2);

        UserRatingUpdateDTO userRatingUpdateDTO = new UserRatingUpdateDTO(2, 5);

        when(userRatingRepository.findByUserAndTagId(user, userRatingUpdateDTO.getTagId())).thenReturn(Optional.empty());
        when(entityManager.getReference(Tag.class, userRatingUpdateDTO.getTagId())).thenReturn(tag);

        UserRating newUserRating = new UserRating();
        newUserRating.setUser(user);
        newUserRating.setTag(tag);
        newUserRating.setRating(userRatingUpdateDTO.getRating());
        UserRatingId newUserRatingId = new UserRatingId(user.getId(), tag.getId());
        newUserRating.setId(newUserRatingId);

        when(userRatingRepository.save(any(UserRating.class))).thenReturn(newUserRating);

        UserRating createdUserRating = userRatingService.update(user, userRatingUpdateDTO);

        assertEquals(newUserRating.getUser(), createdUserRating.getUser());
        assertEquals(newUserRating.getTag(), createdUserRating.getTag());
        assertEquals(userRatingUpdateDTO.getRating(), createdUserRating.getRating());
        assertEquals(newUserRating.getId(), createdUserRating.getId());

        verify(userRatingRepository, times(1)).findByUserAndTagId(user, userRatingUpdateDTO.getTagId());
        verify(userRatingRepository, times(1)).save(any(UserRating.class));
    }

    @Test
    @DisplayName("Get user ratings successfully")
    void getUserRatingsSuccessfully() {
        User user = new User();
        user.setId(1);

        UserRating userRating1 = new UserRating();
        UserRating userRating2 = new UserRating();
        when(userRatingRepository.findAllByUser(user)).thenReturn(List.of(userRating1, userRating2));

        List<UserRating> userRatings = userRatingService.get(user);

        assertEquals(2, userRatings.size());
        assertEquals(userRating1, userRatings.get(0));
        assertEquals(userRating2, userRatings.get(1));

        verify(userRatingRepository, times(1)).findAllByUser(user);
    }
}
