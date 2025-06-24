package com.algonet.algonetapi.services;

import com.algonet.algonetapi.exceptions.NotFoundException;
import com.algonet.algonetapi.models.dto.userProfileDTOs.UserProfileCreationDTO;
import com.algonet.algonetapi.models.dto.userProfileDTOs.UserProfileUpdateDTO;
import com.algonet.algonetapi.models.entities.User;
import com.algonet.algonetapi.models.entities.UserProfile;
import com.algonet.algonetapi.repositories.UserProfileRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static com.algonet.algonetapi.utils.MapperUtils.copyNonNullProperties;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class UserProfileService {
    
    private final UserProfileRepository userProfileRepository;
    
    public UserProfile create(User user, UserProfileCreationDTO dto) {
        log.info("Creating user profile for user: {}", user.getUsername());
        UserProfile profile = new UserProfile();
        BeanUtils.copyProperties(dto, profile);
        profile.setUser(user);
        UserProfile savedProfile = userProfileRepository.save(profile);
        log.info("Successfully created user profile with id: {}", savedProfile.getId());
        return savedProfile;
    }
    
    public UserProfile getByUser(User user) {
        log.debug("Fetching user profile for user: {}", user.getUsername());
        return userProfileRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException("User profile not found"));
    }
    
    public UserProfile update(User user, UserProfileUpdateDTO dto) {
        log.info("Updating user profile for user: {}", user.getUsername());
        UserProfile profile = userProfileRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException("User profile not found"));
        
        copyNonNullProperties(dto, profile);
        UserProfile updatedProfile = userProfileRepository.save(profile);
        log.info("Successfully updated user profile with id: {}", updatedProfile.getId());
        return updatedProfile;
    }
    
    public void updateLastLogin(User user) {
        log.debug("Updating last login time for user: {}", user.getUsername());
        userProfileRepository.findByUser(user).ifPresent(profile -> {
            profile.setLastLoginAt(Instant.now());
            userProfileRepository.save(profile);
        });
    }
}
