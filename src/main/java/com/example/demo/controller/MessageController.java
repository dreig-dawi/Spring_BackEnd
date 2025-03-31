package com.example.demo.controller;

import com.example.demo.model.Message;
import com.example.demo.model.Response;
import com.example.demo.repository.MessageRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageRepository repository;

    public MessageController(MessageRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Message> getAllMessages() {
        return repository.findAll();
    }

    @PostMapping
    public Response createMessage(@RequestBody Message message) {
        if (message.getContent() != null) {
            repository.save(message);
            return new Response(
                    0,
                    message.getContent()
            );
        } else {
            return new Response(
                    1,
                    "Message content cannot be null"
            );
        }
    }

    @PutMapping("/{id}")
    public Message updateMessage(@PathVariable Long id, @RequestBody Message newMessage) {
        return repository.findById(id)
                .map(message -> {
                    message.setContent(newMessage.getContent());
                    return repository.save(message);
                })
                .orElseGet(() -> repository.save(newMessage));
    }

    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
