package com.algonet.algonetapi.controllers;

import com.algonet.algonetapi.annotations.GetAuthUser;
import com.algonet.algonetapi.annotations.ValidateProblemId;
import com.algonet.algonetapi.models.dto.testDTOs.TestCreationDTO;
import com.algonet.algonetapi.models.entities.Test;
import com.algonet.algonetapi.models.entities.User;
import com.algonet.algonetapi.services.TestService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor

@RequestMapping("/api/v1/test")
public class TestController {
    private final TestService testService;
    @ValidateProblemId
    @PostMapping("/problem/{problem_id}")
    public ResponseEntity<Test> createTest(@Parameter(hidden = true) @GetAuthUser User user, @PathVariable Integer problem_id, @RequestBody TestCreationDTO testCreationDTO){
        return new ResponseEntity<>(testService.create(user.getId(), problem_id, testCreationDTO), HttpStatus.OK);

    }
    @ValidateProblemId
    @GetMapping("/problem/{problem_id}")
    public ResponseEntity<List<Test>> getAllTests(@Parameter(hidden = true) @GetAuthUser User user, @PathVariable Integer problem_id){
        return new ResponseEntity<>(testService.getAll(user.getId(), problem_id), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Test> getTest(@Parameter(hidden = true) @GetAuthUser User user, @PathVariable Integer id){
        return new ResponseEntity<>(testService.get(user.getId(), id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Test> updateTest(@Parameter(hidden = true) @GetAuthUser User user, @PathVariable Integer id, @RequestBody TestCreationDTO testCreationDTO){
        return new ResponseEntity<>(testService.update(user.getId(), id, testCreationDTO), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTest(@Parameter(hidden = true) @GetAuthUser User user, @PathVariable Integer id){
        testService.delete(user.getId(), id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
