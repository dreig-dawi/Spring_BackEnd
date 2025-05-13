package com.example.cheffin.service;

import com.example.cheffin.dto.AuthResponseDTO;
import com.example.cheffin.dto.LoginDTO;
import com.example.cheffin.dto.RegisterDTO;
import com.example.cheffin.dto.UserDTO;
import com.example.cheffin.model.User;
import com.example.cheffin.repository.UserRepository;
import com.example.cheffin.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;

    public AuthResponseDTO login(LoginDTO loginDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password");
        }

        // Find user by email
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        // Generate JWT token
        UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);

        return AuthResponseDTO.builder()
                .token(token)
                .user(UserDTO.fromEntity(user))
                .build();
    }

    public AuthResponseDTO register(RegisterDTO registerDTO, boolean isChef) {
        User user = userService.registerUser(registerDTO, isChef);
        
        // Generate JWT token
        UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);

        return AuthResponseDTO.builder()
                .token(token)
                .user(UserDTO.fromEntity(user))
                .build();
    }
}
