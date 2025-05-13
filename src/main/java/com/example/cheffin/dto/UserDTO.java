package com.example.cheffin.dto;

import com.example.cheffin.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String profilePicture;
    private User.Role role;
    private String specialty;
    private String bio;
    private Integer experience;
    private boolean featured;
    
    public static UserDTO fromEntity(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .profilePicture(user.getProfilePicture())
                .role(user.getRole())
                .specialty(user.getSpecialty())
                .bio(user.getBio())
                .experience(user.getExperience())
                .featured(user.isFeatured())
                .build();
    }
}
