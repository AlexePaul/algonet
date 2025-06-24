package com.algonet.algonetapi.services;

import com.algonet.algonetapi.exceptions.NotFoundException;
import com.algonet.algonetapi.models.dto.tagDTOs.TagCreationDTO;
import com.algonet.algonetapi.models.dto.tagDTOs.TagUpdateDTO;
import com.algonet.algonetapi.models.entities.Tag;
import com.algonet.algonetapi.repositories.TagRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class TagService {

    private final TagRepository tagRepository;    public List<Tag> getAll() {
        return tagRepository.findAll();
    }

    public List<Tag> getByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return tagRepository.findAll();
        }
        return tagRepository.findByNameContainingIgnoreCase(name);
    }

    public Tag getById(Integer id) {
        return tagRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public Tag create(TagCreationDTO tagCreationDTO) {
        Tag newTag = new Tag();
        newTag.setName(tagCreationDTO.getName());
        return tagRepository.save(newTag);
    }

    public Tag update(Integer id, TagUpdateDTO tagUpdateDTO) {
        Tag updateTag = new Tag();
        updateTag.setId(id);
        updateTag.setName(tagUpdateDTO.getName());
        return tagRepository.save(updateTag);
    }

    public void delete(Integer id) {
        tagRepository.deleteById(id);
    }
}
