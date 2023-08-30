package com.example.crudoperation.services;


import com.example.crudoperation.entity.Tag;

import java.util.List;

public interface TagService {
    List<Tag> findAll();
    Tag findById(Long theId);
    void save(Tag tag);
    void deleteById(Long theId);

    Object findOrCreateTag(String s);
}
