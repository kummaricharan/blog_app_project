package com.example.crudoperation.services;

import com.example.crudoperation.entity.Comments;

import java.util.List;

public interface CommentService {
    List<Comments> findAll();
    Comments findById(int theId);
    void save(Comments comments);
    void deleteById(int theId);

    Comments findById(Long commentId);

    void deleteById(Long commentId);
}
