package com.algonet.algonetapi.controllers;

import com.algonet.algonetapi.annotations.GetAuthUser;
import com.algonet.algonetapi.models.dto.userProfileDTOs.UserProfileCreationDTO;
import com.algonet.algonetapi.models.dto.userProfileDTOs.UserProfileUpdateDTO;
import com.algonet.algonetapi.models.entities.User;
import com.algonet.algonetapi.models.entities.UserProfile;
import com.algonet.algonetapi.services.UserProfileService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user-profile")
public class UserProfileController {
    
    private final UserProfileService userProfileService;
    
    @PostMapping("")
    public ResponseEntity<UserProfile> create(
            @Parameter(hidden = true) @GetAuthUser User user,
            @Valid @RequestBody UserProfileCreationDTO dto) {
        return new ResponseEntity<>(userProfileService.create(user, dto), HttpStatus.CREATED);
    }
    
    @GetMapping("")
    public ResponseEntity<UserProfile> get(@Parameter(hidden = true) @GetAuthUser User user) {
        return new ResponseEntity<>(userProfileService.getByUser(user), HttpStatus.OK);
    }
    
    @PutMapping("")
    public ResponseEntity<UserProfile> update(
            @Parameter(hidden = true) @GetAuthUser User user,
            @Valid @RequestBody UserProfileUpdateDTO dto) {
        return new ResponseEntity<>(userProfileService.update(user, dto), HttpStatus.OK);
    }
}
