package com.algonet.algonetapi.controllers;

import com.algonet.algonetapi.annotations.GetAuthUser;
import com.algonet.algonetapi.models.dto.UserRatingDTOs.UserRatingUpdateDTO;
import com.algonet.algonetapi.models.entities.User;
import com.algonet.algonetapi.services.UserRatingService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user-rating")
public class UserRatingController {
    private final UserRatingService userRatingService;

    // Updates or creates (if it doesn't exist) a user rating for a tag
    @PutMapping("")
    public ResponseEntity<?> update(@Parameter(hidden = true) @GetAuthUser User user, @RequestBody UserRatingUpdateDTO userRatingUpdateDTO){
        return new ResponseEntity<>(userRatingService.update(user, userRatingUpdateDTO), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<?> get(@Parameter(hidden = true) @GetAuthUser User user){
        return new ResponseEntity<>(userRatingService.get(user), HttpStatus.OK);
    }
}
