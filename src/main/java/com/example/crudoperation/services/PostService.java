package com.example.crudoperation.services;

import com.example.crudoperation.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface PostService {
    List<Post> findAll();

    Post findById(Long theId);

    void save(Post thePost);

    void deleteById(int theId);
    List<Post> Searching(String searchParam);

    List<Post> sortBy(String searchParam);
    Page<Post> findAll(Pageable pageable);
    Page<Post> searchPosts(String keyword, Pageable pageable);
    List<Post> findAllSortedBy(String sortParam, Sort sort);
    List<String> getAllAuthors();

    List<String> getAllTags();
    Page<Post> filterPostsByAuthorsAndTags(Set<String> authors, Set<String> tags,LocalDateTime startDate,LocalDateTime endDate, Pageable pageable);

    List<Post> findAllDraftPost();
    Page<Post> resultWithFilterSearch(String searchParam,Set<String> authors, Set<String> tags,LocalDateTime startDate,LocalDateTime endDate, Pageable pageable);
    List<Post> findAllNotDraftPost();
}
