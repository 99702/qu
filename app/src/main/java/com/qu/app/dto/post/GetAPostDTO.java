package com.qu.app.dto.post;

import com.qu.app.entity.Images;
import lombok.Data;

import java.util.Set;

@Data
public class GetAPostDTO {
    private String authorName;
    private String title;
    private String description;
    private Set<Images> images;
    private Long totalVotes;
}
