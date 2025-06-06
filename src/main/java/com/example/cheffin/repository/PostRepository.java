package com.example.cheffin.repository;

import com.example.cheffin.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    List<Post> findByUserUsername(String username);
    
    List<Post> findAllByOrderByCreatedAtDesc();
    
    @Query(value = "SELECT * FROM posts p ORDER BY RANDOM() LIMIT :count", nativeQuery = true)
    List<Post> findRandomPosts(@Param("count") int count);

    @Query(value = "SELECT * FROM posts p WHERE p.id NOT IN :excludeIds ORDER BY RANDOM() LIMIT :count", nativeQuery = true)
    List<Post> findRandomPostsExcluding(@Param("excludeIds") List<Long> excludeIds, @Param("count") int count);
    
    @Query("SELECT p FROM Post p WHERE " +
           "LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.user.username) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Post> searchPosts(String query);
}
