package com.qu.app.dto.post;

import com.qu.app.entity.Images;
import com.qu.app.entity.User;
import lombok.Data;

import java.util.Set;

@Data
public class GetAPostDTO {
    private String authorName;
    private String title;
    private String description;
    private Set<Images> images;

}
