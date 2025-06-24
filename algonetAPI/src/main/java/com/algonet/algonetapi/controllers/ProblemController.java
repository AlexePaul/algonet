package com.algonet.algonetapi.controllers;

import com.algonet.algonetapi.annotations.GetAuthUser;
import com.algonet.algonetapi.models.dto.problemDTOs.ProblemCreationDTO;
import com.algonet.algonetapi.models.dto.problemDTOs.ProblemPatchDTO;
import com.algonet.algonetapi.models.entities.Problem;
import com.algonet.algonetapi.models.entities.User;
import com.algonet.algonetapi.services.ProblemService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/problems")
public class ProblemController {
    private final ProblemService problemService;

    @PostMapping("")
    public ResponseEntity<Problem> create(@Parameter(hidden = true) @GetAuthUser User user, @RequestBody ProblemCreationDTO problemCreationDTO){
        return new ResponseEntity<>(problemService.create(user, problemCreationDTO, Instant.now()), HttpStatus.OK);
    }    @GetMapping("")
    public ResponseEntity<Page<Problem>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String title) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        if (title != null && !title.trim().isEmpty()) {
            return new ResponseEntity<>(problemService.searchByTitle(title, pageable), HttpStatus.OK);
        }
        
        return new ResponseEntity<>(problemService.getAllPaginated(pageable), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Problem>> searchByTitle(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return new ResponseEntity<>(problemService.searchByTitle(title, pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Problem> get(@PathVariable Integer id){
        return new ResponseEntity<>(problemService.get(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Problem> update(@Parameter(hidden = true) @GetAuthUser User user, @PathVariable Integer id, @RequestBody ProblemPatchDTO problemPatchDTO){
        return new ResponseEntity<>(problemService.update(user.getId(), id, problemPatchDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@Parameter(hidden = true) @GetAuthUser User user, @PathVariable Integer id){
        problemService.delete(user.getId(), id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
