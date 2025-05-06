package com.example.demo.controller;

import com.example.demo.model.Content;
import com.example.demo.model.Post;
import com.example.demo.model.Response;
import com.example.demo.repository.ContentRepository;
import com.example.demo.repository.PostRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content")
public class ContentController {
    private final ContentRepository contentRepository;
    private final PostRepository postRepository;

    public ContentController(ContentRepository contentRepository, PostRepository postRepository) {
        this.contentRepository = contentRepository;
        this.postRepository = postRepository;
    }

    @GetMapping
    public List<Content> getAllContents() {
        return contentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Response getContentById(@PathVariable int id) {
        List<Content> contents = contentRepository.findContentById(id);
        System.out.println(contents);
        String result = contents.toString();
        System.out.println(result);
        return new Response(
                0,
                result
        );
    }

    @PostMapping
    public Response post(@RequestBody Content content) {
        if (content.getData() != null) {
            Post post = postRepository.findPostById(content.getPostIdInput());
            if (post == null) {
                return new Response(1, "Post with id " + content.getPostIdInput() + " not found");
            }
            content.setPostId(post);
            contentRepository.save(content);
            return new Response(
                    0,
                    "Content uploaded successfully"
            );
        } else {
            return new Response(
                    1,
                    "Content cannot be null"
            );
        }
    }
}
