package com.qu.app.dto.post;

import com.qu.app.entity.Images;
import lombok.Data;

import java.util.Set;

@Data
public class PostCreateDTO {
    private String title;
    private String description;
    private Set<Images> images;
}
