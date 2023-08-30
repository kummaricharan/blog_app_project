package com.example.crudoperation.dao;

import com.example.crudoperation.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag,Integer> {
    Optional<Tag> findById(Long theId);

    void deleteById(Long theId);

    Optional<Tag> findByName(String tagName);
}
