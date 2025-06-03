package com.example.cheffin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RandomPostsRequestDTO {
    private int count;
    private List<Long> excludeIds;
}
