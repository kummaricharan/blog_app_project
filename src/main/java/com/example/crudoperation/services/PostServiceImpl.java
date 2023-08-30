package com.example.crudoperation.services;


import com.example.crudoperation.dao.PostRepository;
import com.example.crudoperation.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;


    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }
    @Override
    public Post findById(Long theId) {
       Optional<Post> result= postRepository.findById(theId);
       Post post = null;
       if(result.isPresent()){
           post = result.get();
       }
       else{
           throw new RuntimeException("Did not find employee id - " + theId);
       }
       return post;
    }

    @Override
    public void save(Post thePost) {
        if(thePost.getId()!=null && thePost.isPublished()!=false){
            thePost.setPublishedAt(LocalDateTime.now());
            thePost.setUpdatedAt(LocalDateTime.now());
        }
        else if(thePost.getId()==null) {
            thePost.setPublishedAt(LocalDateTime.now());
        }
        else if(thePost.getId()!=null){
            thePost.setPublishedAt(LocalDateTime.now());
        }
        else{
            thePost.setPublishedAt(LocalDateTime.now());
        }
        postRepository.save(thePost);
    }

    @Override
    public void deleteById(int theId) {
        postRepository.deleteById(theId);
    }

    @Override
    public List<Post> Searching(String searchParam) {
        List<Post> post = postRepository.Searching(searchParam);
        return post ;
    }

    @Override
    public List<Post> sortBy(String searchParam) {
        List<Post> post = postRepository.sortBy(searchParam);
        return post;
    }

    @Override
    public Page<Post> findAll(Pageable pageable) {
        Page<Post> post = postRepository.findAll(pageable);
        return post;
    }
    @Override
    public Page<Post> searchPosts(String keyword, Pageable pageable) {
        return postRepository.searchPosts(keyword, pageable);
    }

    @Override
    public List<Post> findAllSortedBy(String sortParam, Sort sort) {
        return postRepository.findAllSortedBy(sortParam, sort);
    }

    @Override
    public List<String> getAllAuthors() {
        List<String> author = postRepository.getAllAuthors();
        return author;
    }

    @Override
    public List<String> getAllTags() {
        List<String> tag = postRepository.getAllTags();
        return tag;
    }
    @Override
    public Page<Post> filterPostsByAuthorsAndTags(Set<String> authors, Set<String> tags,LocalDateTime startDate,LocalDateTime endDate, Pageable pageable) {
        int isAuthor = authors.size()==0 ? 0 :1;
        int isTag = tags.size()==0 ? 0 :1;
        int isDateTime = (startDate == null || endDate==null) ? 0:1;
        return postRepository.filterPostsByAuthorsAndTags(isAuthor,isTag,isDateTime,authors, tags,startDate,endDate,pageable);
    }

    @Override
    public List<Post> findAllDraftPost() {
        return postRepository.findAllDraftPost();
    }

    @Override
    public Page<Post> resultWithFilterSearch(String searchParam, Set<String> authors, Set<String> tags, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        int isAuthor = authors.size()==0 ? 0 :1;
        int isTag = tags.size()==0 ? 0 :1;
        int isDateTime = (startDate == null || endDate==null) ? 0:1;
        return postRepository.resultWithFilterSearch(searchParam,isAuthor,isTag,isDateTime,authors, tags,startDate,endDate,pageable);
    }

    @Override
    public List<Post> findAllNotDraftPost() {
        return postRepository.findAllNotDraftPost();
    }

}
