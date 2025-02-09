package com.algonet.algonetapi.unit;

import com.algonet.algonetapi.models.dto.tagDTOs.TagCreationDTO;
import com.algonet.algonetapi.models.dto.tagDTOs.TagUpdateDTO;
import com.algonet.algonetapi.models.entities.Tag;
import com.algonet.algonetapi.repositories.TagRepository;
import com.algonet.algonetapi.services.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Create tag successfully")
    void createTagSuccessfully() {
        Tag tag = new Tag();
        tag.setId(1);
        TagCreationDTO tagCreationDTO = new TagCreationDTO("name");
        tag.setName(tagCreationDTO.getName());

        when(tagRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Tag createdTag = tagService.create(tagCreationDTO);
        createdTag.setId(1);

        assertEquals(tag, createdTag);
    }

    @Test
    @DisplayName("Update tag successfully")
    void updateTagSuccessfully() {
        Tag tag = new Tag();
        tag.setId(1);
        tag.setName("name");
        TagUpdateDTO tagUpdateDTO = new TagUpdateDTO("new name");

        when(tagRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Tag updatedTag = tagService.update(tag.getId(), tagUpdateDTO);
        updatedTag.setId(1);

        assertNotEquals(tag, updatedTag);
        assertEquals(tagUpdateDTO.getName(), updatedTag.getName());
    }

    @Test
    @DisplayName("Delete tag successfully")
    void deleteTagSuccessfully() {
        Tag tag = new Tag();
        tag.setId(1);
        tag.setName("name");

        doNothing().when(tagRepository).deleteById(tag.getId());

        tagService.delete(tag.getId());

        verify(tagRepository, times(1)).deleteById(tag.getId());
    }

    @Test
    @DisplayName("Get tag by id successfully")
    void getTagByIdSuccessfully() {
        Tag tag = new Tag();
        tag.setId(1);
        tag.setName("name");

        when(tagRepository.findById(tag.getId())).thenReturn(java.util.Optional.of(tag));

        Tag gottenTag = tagService.getById(tag.getId());

        assertEquals(tag, gottenTag);
    }

    @Test
    @DisplayName("Get all tags successfully")
    void getAllTagsSuccessfully() {
        Tag tag1 = new Tag();
        tag1.setId(1);
        tag1.setName("name1");

        Tag tag2 = new Tag();
        tag2.setId(2);
        tag2.setName("name2");

        when(tagRepository.findAll()).thenReturn(java.util.List.of(tag1, tag2));

        assertEquals(java.util.List.of(tag1, tag2), tagService.getAll());
    }
}
