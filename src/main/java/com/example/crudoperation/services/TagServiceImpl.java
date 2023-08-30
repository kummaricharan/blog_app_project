package com.example.crudoperation.services;

import com.example.crudoperation.dao.TagRepository;
import com.example.crudoperation.entity.Tag;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class TagServiceImpl implements TagService{
    private TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    @Override
    public Tag findById(Long theId) {
        Optional<Tag> result = tagRepository.findById(theId);
        Tag tag = null;
        if(result.isPresent()){
            tag = result.get();
        }
        else{
            throw new RuntimeException("Did not find employee id - " + theId);
        }
        return tag;
    }

    @Override
    public void save(Tag tag) {
        if(tag.getId()!=null){
            tag.setUpdatedAt(LocalDateTime.now());
        } else {
            tag.getCreatedAt(LocalDateTime.now());
        }
        tagRepository.save(tag);
    }

    @Override
    public void deleteById(Long theId) {
        tagRepository.deleteById(theId);
    }

    @Override
    public Tag findOrCreateTag(String tagName) {
        Optional<Tag> existingTag = tagRepository.findByName(tagName);

        if (existingTag.isPresent()) {
            return existingTag.get();
        } else {
            Tag newTag = new Tag(tagName);
            return tagRepository.save(newTag);
        }
    }


}
