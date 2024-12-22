package com.algonet.algonetapi.controllers;

import com.algonet.algonetapi.Models.dto.ProblemTestCreationDTO;
import com.algonet.algonetapi.Models.entities.Problem;
import com.algonet.algonetapi.services.ProblemService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/problem")
public class ProblemController {
    private ProblemService problemService;

    @PostMapping("/create")
    public ResponseEntity<Problem> create(@RequestBody ProblemTestCreationDTO request) {
        return new ResponseEntity<>(problemService.create(request.getProblem(), request.getTests()), HttpStatus.OK);
    }
}
