package com.example.demo.repository;

import com.example.demo.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {
    @Query(value = "SELECT * FROM content WHERE post_id = ?1", nativeQuery = true)
    List<Content> findContentById(int postId);
}