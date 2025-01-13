package com.algonet.algonetapi.controllers;

import com.algonet.algonetapi.models.dto.tagDTOs.TagCreationDTO;
import com.algonet.algonetapi.models.dto.tagDTOs.TagUpdateDTO;
import com.algonet.algonetapi.models.entities.Tag;
import com.algonet.algonetapi.services.TagService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/tag")
public class TagController {
    private TagService tagService;

    @GetMapping("")
    public ResponseEntity<List<Tag>> getAll(){
        return new ResponseEntity<>(tagService.getAll(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Tag> getById(@PathVariable Integer id){
        return new ResponseEntity<>(tagService.getById(id),HttpStatus.OK);
    }
    @PostMapping("")
    public ResponseEntity<Tag> create(@RequestBody TagCreationDTO tagCreationDTO){
        return new ResponseEntity<>(tagService.create(tagCreationDTO), HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Tag> update(@PathVariable Integer id, @RequestBody TagUpdateDTO tagUpdateDTO){
        return new ResponseEntity<>(tagService.update(id, tagUpdateDTO), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        tagService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
