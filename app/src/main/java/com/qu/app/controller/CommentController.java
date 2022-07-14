package com.qu.app.controller;

import com.qu.app.dto.comment.request.CreateCommentRequest;
import com.qu.app.dto.comment.response.CreateCommentResponse;
import com.qu.app.dto.post.PostCreateDTO;
import com.qu.app.dto.user.LoggedInUser;
import com.qu.app.enumeration.PathConstant;
import com.qu.app.service.CommentService;
import com.qu.app.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping(PathConstant.CREATE_COMMENT)
    public CreateCommentResponse createPost(@RequestBody CreateCommentRequest createCommentRequest, @PathVariable("postId") Long postId){
        return commentService.createComment(createCommentRequest, postId);
    }




//    private final SessionService sessionService;
//    public CommentController(SessionService sessionService) {
//        this.sessionService = sessionService;
//    }
//
//    public void postComment(Long userId, String comment, int likeCount) {
////       if () {
//           if (sessionService.loggedInUser().getUserId().equals(userId)) {
//               likeCount = likeCount - 1;
//           } else {
//               likeCount = likeCount + 1;
//           }
////       }
//    }
}
