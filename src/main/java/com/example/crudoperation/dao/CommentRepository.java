package com.example.crudoperation.dao;

import com.example.crudoperation.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comments, Long> {

    Optional<Comments> findById(int theId);

    void deleteById(int theId);


}
