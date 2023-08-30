package com.example.crudoperation.dao;

import com.example.crudoperation.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post,Integer> {

    Optional<Post> findById(Long theId);

    @Query("SELECT DISTINCT p FROM Post p " +
            "LEFT JOIN p.tags t " +
            "WHERE p.title LIKE %:param% " +
            "OR p.content LIKE %:param% " +
            "OR p.author LIKE %:param% " +
            "OR t.name LIKE %:param%")
    List<Post> Searching(@Param("param") String query);

    @Query("SELECT p FROM Post p WHERE p.isPublished = true AND lower(p.title) LIKE lower(concat('%', :keyword, '%')) " +
            "OR lower(p.content) LIKE lower(concat('%', :keyword, '%'))"+
            "OR lower(p.author) LIKE lower(concat('%', :keyword, '%'))")
    Page<Post> searchPosts(@Param("keyword") String keyword, Pageable pageable);

    List<Post> findAllSortedBy(String sortParam, Sort sort);

    @Query("SELECT p FROM Post p ORDER BY "
            + "CASE WHEN :param = 'title' THEN p.title END DESC, "
            + "CASE WHEN :param = 'publishedAt' THEN p.publishedAt END, "
            + "CASE WHEN :param = 'author' THEN p.author END DESC")
    List<Post> sortBy(@Param("param") String searchParam);

    @Query("SELECT DISTINCT p.author FROM Post p")
    List<String> getAllAuthors();

    @Query("SELECT DISTINCT t.name FROM Tag t")
    List<String> getAllTags();

    @Query("SELECT DISTINCT p FROM Post p " +
            "WHERE p.isPublished = true " +
            "AND (:isAuthor = 0 OR p.author IN :authors) " +
            "AND (:isTag = 0 OR EXISTS (SELECT t FROM p.tags t WHERE t.name IN :tags)) " +
            "AND (:isDateTime =0 OR p.publishedAt BETWEEN :startDate AND :endDate)")
    Page<Post> filterPostsByAuthorsAndTags(
            @Param("isAuthor") int isAuthor,
            @Param("isTag") int isTag,
            @Param("isDateTime") int isDateTime,
            @Param("authors") Set<String> authors,
            @Param("tags") Set<String> tags,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);
    @Query("SELECT p FROM Post p WHERE p.isPublished = false")
    List<Post> findAllDraftPost();
    @Query("SELECT p FROM Post p WHERE p.isPublished = true" )
    List<Post> findAllNotDraftPost();
    @Query("SELECT p FROM Post p WHERE p.isPublished = true")
    Page<Post> findAll(Pageable pageable);
    @Query("SELECT DISTINCT p FROM Post p " +
            "LEFT JOIN p.tags t " +
            "WHERE ((p.title LIKE %:searchParam%) " +
            "OR (p.content LIKE %:searchParam%) " +
            "OR (p.author LIKE %:searchParam%) " +
            "OR (t.name LIKE %:searchParam%)) " +
            "AND (:isAuthor = 0 OR p.author IN :authors) " +
            "AND (:isTag = 0 OR EXISTS (SELECT t FROM p.tags t WHERE t.name IN :tags)) " +
            "AND (:isDateTime = 0 OR p.publishedAt BETWEEN :startDate AND :endDate) " +
            "AND p.isPublished = true")
    Page<Post> resultWithFilterSearch(
            @Param("searchParam") String searchParam,
            @Param("isAuthor") int isAuthor,
            @Param("isTag") int isTag,
            @Param("isDateTime") int isDateTime,
            @Param("authors") Set<String> authors,
            @Param("tags") Set<String> tags,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

}
