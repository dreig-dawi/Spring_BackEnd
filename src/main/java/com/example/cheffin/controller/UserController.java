package com.example.cheffin.controller;

import com.example.cheffin.dto.AuthResponseDTO;
import com.example.cheffin.dto.LoginDTO;
import com.example.cheffin.dto.RegisterDTO;
import com.example.cheffin.dto.UserDTO;
import com.example.cheffin.service.AuthService;
import com.example.cheffin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(authService.login(loginDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterDTO registerDTO) {
        return ResponseEntity.ok(authService.register(registerDTO, false));
    }

    @PostMapping("/register/chef")
    public ResponseEntity<AuthResponseDTO> registerChef(@RequestBody RegisterDTO registerDTO) {
        return ResponseEntity.ok(authService.register(registerDTO, true));
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getCurrentUserProfile(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(userService.getUserProfile(username));
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserProfile(username));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserDTO> updateProfile(Authentication authentication, @RequestBody UserDTO userDTO) {
        String username = authentication.getName();
        return ResponseEntity.ok(userService.updateProfile(username, userDTO));
    }

    @GetMapping("/chefs/featured")
    public ResponseEntity<List<UserDTO>> getFeaturedChefs() {
        return ResponseEntity.ok(userService.getFeaturedChefs());
    }    
    
    @GetMapping("/chefs")
    public ResponseEntity<List<UserDTO>> getAllChefs() {
        return ResponseEntity.ok(userService.getAllChefs());
    }
    
    @GetMapping("/chefs/search")
    public ResponseEntity<Map<String, Object>> searchChefs(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.searchChefs(query, page, size));
    }
    
    @GetMapping("/chefs/random")
    public ResponseEntity<List<UserDTO>> getRandomChefs(
            @RequestParam(defaultValue = "6") int count) {
        return ResponseEntity.ok(userService.getRandomChefs(count));
    }
    
    @GetMapping("/chef/{username}")
    public ResponseEntity<UserDTO> getChefProfile(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserProfile(username));
    }
}
