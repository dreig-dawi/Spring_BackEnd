package com.example.cheffin.service;

import com.example.cheffin.dto.RegisterDTO;
import com.example.cheffin.dto.UserDTO;
import com.example.cheffin.exception.ResourceNotFoundException;
import com.example.cheffin.model.User;
import com.example.cheffin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        
        // Try to find by username first
        if (userRepository.existsByUsername(username)) {
            user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        } 
        // If not found by username, try email
        else if (userRepository.existsByEmail(username)) {
            user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        } 
        else {
            throw new UsernameNotFoundException("User not found with username or email: " + username);
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>());
    }

    public UserDTO getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return UserDTO.fromEntity(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public User registerUser(RegisterDTO registerDTO, boolean isChef) {
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = User.builder()
                .username(registerDTO.getUsername())
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .profilePicture(registerDTO.getProfilePicture())
                .role(isChef ? User.Role.CHEF : User.Role.USER)
                .build();

        if (isChef) {
            user.setSpecialty(registerDTO.getSpecialty());
            user.setBio(registerDTO.getBio());
            user.setExperience(registerDTO.getExperience());
        }

        return userRepository.save(user);
    }

    public UserDTO updateProfile(String username, UserDTO userDTO) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        if (userDTO.getProfilePicture() != null) {
            user.setProfilePicture(userDTO.getProfilePicture());
        }

        if (user.getRole() == User.Role.CHEF) {
            if (userDTO.getSpecialty() != null) {
                user.setSpecialty(userDTO.getSpecialty());
            }
            if (userDTO.getBio() != null) {
                user.setBio(userDTO.getBio());
            }
            if (userDTO.getExperience() != null) {
                user.setExperience(userDTO.getExperience());
            }
        }

        User updatedUser = userRepository.save(user);
        return UserDTO.fromEntity(updatedUser);
    }

    public List<UserDTO> getFeaturedChefs() {
        return userRepository.findFeaturedChefs().stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getAllChefs() {
        return userRepository.findAllChefs().stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
