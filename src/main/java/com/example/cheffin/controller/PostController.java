package com.example.cheffin.controller;

import com.example.cheffin.dto.PostDTO;
import com.example.cheffin.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<PostDTO>> getPostsByUser(@PathVariable String username) {
        return ResponseEntity.ok(postService.getPostsByUsername(username));
    }

    @PostMapping
    public ResponseEntity<PostDTO> createPost(Authentication authentication, @RequestBody PostDTO postDTO) {
        String username = authentication.getName();
        return ResponseEntity.ok(postService.createPost(username, postDTO));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostDTO> updatePost(
            Authentication authentication,
            @PathVariable Long postId,
            @RequestBody PostDTO postDTO) {
        String username = authentication.getName();
        return ResponseEntity.ok(postService.updatePost(postId, username, postDTO));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(Authentication authentication, @PathVariable Long postId) {
        String username = authentication.getName();
        postService.deletePost(postId, username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostDTO>> searchPosts(@RequestParam String query) {
        return ResponseEntity.ok(postService.searchPosts(query));
    }
}
