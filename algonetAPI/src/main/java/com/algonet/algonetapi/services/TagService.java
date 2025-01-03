package com.algonet.algonetapi.services;

import com.algonet.algonetapi.models.entities.Tag;
import com.algonet.algonetapi.exceptions.NotFoundException;
import com.algonet.algonetapi.repositories.TagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    public List<Tag> getAll() {
        return tagRepository.findAll();
    }

    public Tag getById(Integer id) {
        return tagRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public Tag create(String name) {
        Tag newTag = new Tag();
        newTag.setName(name);
        return tagRepository.save(newTag);
    }

    public Tag update(Integer id, String name) {
        Tag updateTag = new Tag();
        updateTag.setId(id);
        updateTag.setName(name);
        return tagRepository.save(updateTag);
    }

    public void delete(Integer id) {
        tagRepository.deleteById(id);
    }
}
