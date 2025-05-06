package com.example.demo.controller;

import com.example.demo.model.Response;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository repository;
    
    public UserController(UserRepository repository) {
        this.repository = repository;
    }
    
    @GetMapping
    public List<User> getAllUsers() {
        return repository.findAll();
    }
    
    @GetMapping("/{id}")
    public Response getUserById(@PathVariable String id) {
        return new Response(
                0,
                repository.findUserById(id).toString()
        );
    }
    
    @PostMapping
    public Response createUser(@RequestBody User user) {
        if (
                user.getUsername() != null &&
                user.getEmail() != null &&
                user.getPasswordHash() != null
        ) {
            repository.save(user);
            return new Response(
                    0,
                    user.getUsername()
            );
        } else {
            return new Response(
                    1,
                    "User content cannot be null"
            );
        }
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User newUser) {
        return repository.findById(id)
                .map(user -> {
                    user.setEmail(newUser.getEmail());
                    user.setUsername(newUser.getUsername());
                    user.setPasswordHash(newUser.getPasswordHash());
                    return repository.save(user);
                })
                .orElseGet(() -> repository.save(newUser));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
