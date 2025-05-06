package com.example.demo.controller;

import com.example.demo.model.Content;
import com.example.demo.model.Response;
import com.example.demo.repository.ContentRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/content")
public class ContentController {
    private final ContentRepository contentRepository;

    public ContentController(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    @PostMapping
    public Response post(@RequestBody Content content) {
        if (content.getData() != null) {
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
