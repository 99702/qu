package com.qu.app.dto.post.response;

import lombok.Data;


@Data
public class PostUpdateResponseDTO {
    private String title;
    private String description;
    private String authorName;
}
