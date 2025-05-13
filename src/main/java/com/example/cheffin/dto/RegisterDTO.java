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
public class RegisterDTO {
    private String username;
    private String email;
    private String password;
    private String profilePicture;
    // Chef specific fields
    private String specialty;
    private String bio;
    private Integer experience;
}
