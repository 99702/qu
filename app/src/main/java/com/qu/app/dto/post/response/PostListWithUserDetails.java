package com.qu.app.dto.post.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
public class PostListWithUserDetails {
    private Long postId;
    private String postDescription;
    private String postTitle;
    private Long authorId;
    private String authorName;
    private String authorImage;
}
