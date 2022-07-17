package com.qu.app.service;

import com.qu.app.dto.comment.request.CreateCommentRequest;
import com.qu.app.dto.comment.response.CreateCommentResponse;

public interface CommentService {
    CreateCommentResponse createComment(CreateCommentRequest createCommentRequest, Long postId);
}
