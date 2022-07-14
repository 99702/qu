package com.qu.app.service;

import com.qu.app.dto.comment.request.CreateCommentRequest;
import com.qu.app.dto.comment.response.CreateCommentResponse;
import com.qu.app.dto.post.PostCreateDTO;

public interface CommentService {

    CreateCommentResponse createComment(CreateCommentRequest createCommentRequest, Long postId);
}
