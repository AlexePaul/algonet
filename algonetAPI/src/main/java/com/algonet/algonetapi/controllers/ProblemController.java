package com.algonet.algonetapi.controllers;

import com.algonet.algonetapi.annotations.GetAuthUser;
import com.algonet.algonetapi.models.dto.problemDTOs.ProblemCreationDTO;
import com.algonet.algonetapi.models.dto.problemDTOs.ProblemPatchDTO;
import com.algonet.algonetapi.models.entities.Problem;
import com.algonet.algonetapi.models.entities.User;
import com.algonet.algonetapi.services.ProblemService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/problem")
public class ProblemController {
    private ProblemService problemService;

    @PostMapping("/create")
    public ResponseEntity<Problem> create(@Parameter(hidden = true) @GetAuthUser User user, @RequestBody ProblemCreationDTO problemCreationDTO){
        return new ResponseEntity<>(problemService.create(user, problemCreationDTO, Instant.now()), HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Problem> get(@PathVariable Integer id){
        return new ResponseEntity<>(problemService.get(id), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Problem> update(@Parameter(hidden = true) @GetAuthUser User user, @PathVariable Integer id, @RequestBody ProblemPatchDTO problemPatchDTO){
        return new ResponseEntity<>(problemService.update(user, id, problemPatchDTO), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@Parameter(hidden = true) @GetAuthUser User user, @PathVariable Integer id){
        problemService.delete(user, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
