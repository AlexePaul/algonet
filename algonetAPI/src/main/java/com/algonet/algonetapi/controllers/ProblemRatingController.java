package com.algonet.algonetapi.controllers;

import com.algonet.algonetapi.annotations.GetAuthUser;
import com.algonet.algonetapi.models.dto.ProblemRatingDTOs.ProblemRatingUpdateDTO;
import com.algonet.algonetapi.models.entities.User;
import com.algonet.algonetapi.services.ProblemRatingService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/problem-rating")
public class ProblemRatingController {
    private final ProblemRatingService problemRatingService;

    @PutMapping("")
    public ResponseEntity<?> update(@Parameter(hidden = true) @GetAuthUser User user, @RequestBody ProblemRatingUpdateDTO problemRatingUpdateDTO){
        return new ResponseEntity<>(problemRatingService.update(user, problemRatingUpdateDTO), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Integer id){
        return new ResponseEntity<>(problemRatingService.get(id), HttpStatus.OK);
    }
}
