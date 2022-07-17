package com.qu.app.dto.post.response;

import lombok.AllArgsConstructor;
import lombok.Builder;

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
