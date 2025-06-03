package com.example.cheffin.service;

import com.example.cheffin.dto.PostDTO;
import com.example.cheffin.exception.ResourceNotFoundException;
import com.example.cheffin.model.Post;
import com.example.cheffin.model.User;
import com.example.cheffin.repository.PostRepository;
import com.example.cheffin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<PostDTO> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(PostDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PostDTO> getRandomPosts(int count, List<Long> excludeIds) {
        List<Post> randomPosts;
        if (excludeIds != null && !excludeIds.isEmpty()) {
            randomPosts = postRepository.findRandomPostsExcluding(excludeIds, count);
        } else {
            randomPosts = postRepository.findRandomPosts(count);
        }
        return randomPosts.stream()
                .map(PostDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PostDTO> getPostsByUsername(String username) {
        return postRepository.findByUserUsername(username).stream()
                .map(PostDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public PostDTO createPost(String username, PostDTO postDTO) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        Post post = Post.builder()
                .user(user)
                .title(postDTO.getTitle())
                .description(postDTO.getDescription())
                .contentImages(postDTO.getContentImages())
                .build();

        Post savedPost = postRepository.save(post);
        return PostDTO.fromEntity(savedPost);
    }

    public PostDTO updatePost(Long postId, String username, PostDTO postDTO) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        // Check if the post belongs to the user
        if (!post.getUser().getUsername().equals(username)) {
            throw new IllegalStateException("You can only update your own posts");
        }

        if (postDTO.getTitle() != null) {
            post.setTitle(postDTO.getTitle());
        }
        if (postDTO.getDescription() != null) {
            post.setDescription(postDTO.getDescription());
        }
        if (postDTO.getContentImages() != null) {
            post.setContentImages(postDTO.getContentImages());
        }

        Post updatedPost = postRepository.save(post);
        return PostDTO.fromEntity(updatedPost);
    }

    public void deletePost(Long postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        // Check if the post belongs to the user
        if (!post.getUser().getUsername().equals(username)) {
            throw new IllegalStateException("You can only delete your own posts");
        }

        postRepository.delete(post);
    }

    public List<PostDTO> searchPosts(String query) {
        return postRepository.searchPosts(query).stream()
                .map(PostDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
