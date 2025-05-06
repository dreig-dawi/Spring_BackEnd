package com.example.demo.controller;

import com.example.demo.model.Post;
import com.example.demo.model.Response;
import com.example.demo.model.User;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostRepository repository;
    private final UserRepository userRepository;

    public PostController(PostRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Post> getPosts() {
        return repository.getPosts();
    }

    @PostMapping
    public Response createPost(@RequestBody Post post) {
        if (
                post.getDescription() != null &&
                post.getUserEmail() != null
        ) {
            User user  = userRepository.findUserById(post.getUserEmail());
            if (user == null) {
                return new Response(1, "User with email " + post.getUserEmail() + " not found");
            }
            post.setUser(user);
            repository.save(post);
            return new Response(
                    0,
                    post.getDescription()
            );
        } else {
            return new Response(
                    1,
                    "Post content cannot be null"
            );
        }
    }
}
