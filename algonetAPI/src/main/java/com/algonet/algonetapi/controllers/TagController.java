package com.algonet.algonetapi.controllers;

import com.algonet.algonetapi.models.dto.tagDTOs.TagCreationDTO;
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

    @GetMapping("/getAll")
    public ResponseEntity<List<Tag>> getAll(){
        return new ResponseEntity<>(tagService.getAll(), HttpStatus.OK);
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<Tag> getById(@RequestParam Integer id){
        return new ResponseEntity<>(tagService.getById(id),HttpStatus.OK);
    }
    @PostMapping("/create")
    public ResponseEntity<Tag> create(@RequestBody TagCreationDTO tagCreationDTO){
        return new ResponseEntity<>(tagService.create(tagCreationDTO), HttpStatus.OK);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Tag> update(@RequestParam Integer id, @RequestBody String name){
        return new ResponseEntity<>(tagService.update(id, name), HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@RequestParam Integer id){
        tagService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
