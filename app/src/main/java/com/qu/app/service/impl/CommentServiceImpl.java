package com.qu.app.service.impl;

import com.qu.app.dto.comment.request.CreateCommentRequest;
import com.qu.app.dto.comment.response.CreateCommentResponse;
import com.qu.app.service.CommentService;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
    public CreateCommentResponse createComment(CreateCommentRequest createCommentRequest, Long posId){
        System.out.println("Comment is hitted.");
        return null;
    }

}
