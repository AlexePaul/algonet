package com.algonet.algonetapi.unit;

import com.algonet.algonetapi.exceptions.NotFoundException;
import com.algonet.algonetapi.models.dto.userProfileDTOs.UserProfileCreationDTO;
import com.algonet.algonetapi.models.dto.userProfileDTOs.UserProfileUpdateDTO;
import com.algonet.algonetapi.models.entities.User;
import com.algonet.algonetapi.models.entities.UserProfile;
import com.algonet.algonetapi.repositories.UserProfileRepository;
import com.algonet.algonetapi.services.UserProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private UserProfileService userProfileService;

    private User testUser;
    private UserProfile testProfile;
    private UserProfileCreationDTO creationDTO;
    private UserProfileUpdateDTO updateDTO;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");

        testProfile = new UserProfile();
        testProfile.setId(1);
        testProfile.setUser(testUser);        testProfile.setFirstName("John");
        testProfile.setLastName("Doe");
        testProfile.setBio("Test bio");
        testProfile.setLocation("New York");
        testProfile.setWebsite("johndoe.com");

        creationDTO = new UserProfileCreationDTO();
        creationDTO.setFirstName("John");
        creationDTO.setLastName("Doe");
        creationDTO.setBio("Test bio");
        creationDTO.setLocation("New York");
        creationDTO.setWebsite("johndoe.com");

        updateDTO = new UserProfileUpdateDTO();
        updateDTO.setFirstName("Jane");
        updateDTO.setBio("Updated bio");
    }

    @Test
    void create_ShouldCreateUserProfile_WhenValidData() {
        // Given
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(testProfile);

        // When
        UserProfile result = userProfileService.create(testUser, creationDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUser()).isEqualTo(testUser);
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getLastName()).isEqualTo("Doe");
        verify(userProfileRepository).save(any(UserProfile.class));
    }

    @Test
    void getByUser_ShouldReturnUserProfile_WhenProfileExists() {
        // Given
        when(userProfileRepository.findByUser(testUser)).thenReturn(Optional.of(testProfile));

        // When
        UserProfile result = userProfileService.getByUser(testUser);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(testProfile);
        verify(userProfileRepository).findByUser(testUser);
    }

    @Test
    void getByUser_ShouldThrowNotFoundException_WhenProfileNotExists() {
        // Given
        when(userProfileRepository.findByUser(testUser)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userProfileService.getByUser(testUser))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User profile not found");
        verify(userProfileRepository).findByUser(testUser);
    }

    @Test
    void update_ShouldUpdateUserProfile_WhenValidData() {
        // Given
        when(userProfileRepository.findByUser(testUser)).thenReturn(Optional.of(testProfile));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(testProfile);

        // When
        UserProfile result = userProfileService.update(testUser, updateDTO);

        // Then
        assertThat(result).isNotNull();
        verify(userProfileRepository).findByUser(testUser);
        verify(userProfileRepository).save(any(UserProfile.class));
    }

    @Test
    void update_ShouldThrowNotFoundException_WhenProfileNotExists() {
        // Given
        when(userProfileRepository.findByUser(testUser)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userProfileService.update(testUser, updateDTO))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User profile not found");
        verify(userProfileRepository).findByUser(testUser);
        verify(userProfileRepository, never()).save(any());
    }    @Test
    void updateLastLogin_ShouldUpdateLastLoginTime_WhenProfileExists() {
        // Given
        when(userProfileRepository.findByUser(testUser)).thenReturn(Optional.of(testProfile));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(testProfile);

        // When
        userProfileService.updateLastLogin(testUser);

        // Then
        verify(userProfileRepository).findByUser(testUser);
        verify(userProfileRepository).save(testProfile);
    }

    @Test
    void updateLastLogin_ShouldDoNothing_WhenProfileNotExists() {
        // Given
        when(userProfileRepository.findByUser(testUser)).thenReturn(Optional.empty());

        // When
        userProfileService.updateLastLogin(testUser);

        // Then
        verify(userProfileRepository).findByUser(testUser);
        verify(userProfileRepository, never()).save(any());
    }
}
