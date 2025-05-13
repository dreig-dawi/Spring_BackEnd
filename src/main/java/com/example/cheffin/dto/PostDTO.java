package com.example.cheffin.dto;

import com.example.cheffin.model.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long id;
    private String username;
    private String title;
    private String description;
    private List<String> contentImages;
    private LocalDateTime createdAt;
    
    public static PostDTO fromEntity(Post post) {
        return PostDTO.builder()
                .id(post.getId())
                .username(post.getUser().getUsername())
                .title(post.getTitle())
                .description(post.getDescription())
                .contentImages(post.getContentImages())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
