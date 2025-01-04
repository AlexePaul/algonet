package com.algonet.algonetapi.controllers;

import com.algonet.algonetapi.annotations.GetAuthUser;
import com.algonet.algonetapi.annotations.ValidateProblemId;
import com.algonet.algonetapi.models.dto.solutionDTOs.SolutionCreationDTO;
import com.algonet.algonetapi.models.entities.User;
import com.algonet.algonetapi.services.SolutionService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/solution")
public class SolutionController {
    private SolutionService solutionService;

    @PostMapping("/create/problem/{problem_id}")
    @ValidateProblemId
    public ResponseEntity<?> create(@Parameter(hidden = true) @GetAuthUser User user, @PathVariable Integer problem_id, @RequestBody SolutionCreationDTO solutionCreationDTO){
        System.out.println(user.getId());
        return new ResponseEntity<>(solutionService.create(user, problem_id, solutionCreationDTO), HttpStatus.OK);
    }
    @GetMapping("/get/problem/{problem_id}")
    @ValidateProblemId
    public ResponseEntity<?> getByProblemIdAndUserId(@Parameter(hidden = true) @GetAuthUser User user, @PathVariable Integer problem_id){
        return new ResponseEntity<>(solutionService.getByUserAndProblemId(user, problem_id), HttpStatus.OK);
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<?> get(@PathVariable Integer id){
        return new ResponseEntity<>(solutionService.get(id), HttpStatus.OK);
    }

}