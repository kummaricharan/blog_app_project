package com.example.crudoperation.services;

import com.example.crudoperation.dao.CommentRepository;
import com.example.crudoperation.entity.Comments;
import com.example.crudoperation.entity.Post;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comments> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public Comments findById(int theId) {
        Optional<Comments> result= commentRepository.findById(theId);
        Comments comments = null;
        if(result.isPresent()){
            comments = result.get();
        }
        else{
            throw new RuntimeException("Did not find employee id - " + theId);
        }
        return comments;
    }

    @Override
    public void save(Comments comments) {
        commentRepository.save(comments);
    }

    @Override
    public void deleteById(int theId) {
        commentRepository.deleteById(theId);
    }

    @Override
    public Comments findById(Long commentId) {
        Optional<Comments> result= commentRepository.findById(commentId);
        Comments comments = null;
        if(result.isPresent()){
            comments = result.get();
        }
        else{
            throw new RuntimeException("Did not find employee id - " +commentId);
        }
        return comments;
    }

    @Override
    public void deleteById(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
