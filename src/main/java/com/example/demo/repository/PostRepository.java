package com.example.demo.repository;

import com.example.demo.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = "SELECT * FROM post ORDER BY RANDOM() LIMIT 10", nativeQuery = true)
    List<Post> getPosts();

    @Query(value = "SELECT * FROM post WHERE id = ?1", nativeQuery = true)
    Post findPostById(int id);
}