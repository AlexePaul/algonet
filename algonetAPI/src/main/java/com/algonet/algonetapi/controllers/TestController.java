package com.algonet.algonetapi.controllers;

import com.algonet.algonetapi.annotations.ValidateProblemId;
import com.algonet.algonetapi.models.dto.testDTOs.TestCreationDTO;
import com.algonet.algonetapi.models.entities.Test;
import com.algonet.algonetapi.services.TestService;
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
    public ResponseEntity<Test> createTest(@PathVariable Integer problem_id, @RequestBody TestCreationDTO testCreationDTO){
        return new ResponseEntity<>(testService.create(problem_id, testCreationDTO), HttpStatus.OK);

    }
    @ValidateProblemId
    @GetMapping("/problem/{problem_id}")
    public ResponseEntity<List<Test>> getAllTests(@PathVariable Integer problem_id){
        return new ResponseEntity<>(testService.getAll(problem_id), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Test> getTest(@PathVariable Integer id){
        return new ResponseEntity<>(testService.get(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Test> updateTest(@PathVariable Integer id, @RequestBody TestCreationDTO testCreationDTO){
        return new ResponseEntity<>(testService.update(id, testCreationDTO), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTest(@PathVariable Integer id){
        testService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
