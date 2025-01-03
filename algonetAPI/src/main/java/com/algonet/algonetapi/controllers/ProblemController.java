package com.algonet.algonetapi.controllers;

import com.algonet.algonetapi.Models.dto.problemDTOs.ProblemCreationDTO;
import com.algonet.algonetapi.Models.dto.problemDTOs.ProblemPatchDTO;
import com.algonet.algonetapi.Models.entities.Problem;
import com.algonet.algonetapi.services.ProblemService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/problem")
public class ProblemController {
    private ProblemService problemService;

    @PostMapping("/create")
    public ResponseEntity<Problem> create(@RequestBody ProblemCreationDTO problemCreationDTO){
        return new ResponseEntity<>(problemService.create(problemCreationDTO), HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Problem> get(@PathVariable Integer id){
        return new ResponseEntity<>(problemService.get(id), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Problem> update(@PathVariable Integer id, @RequestBody ProblemPatchDTO problemPatchDTO){
        return new ResponseEntity<>(problemService.update(id, problemPatchDTO), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        problemService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
