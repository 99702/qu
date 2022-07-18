package com.qu.app.controller;

import com.qu.app.dto.post.GetAPostDTO;
import com.qu.app.dto.postVotes.response.GetAListOfUserWhoVotedThatPostDTO;
import com.qu.app.dto.postVotes.response.VoteAPostResponseDTO;
import com.qu.app.enumeration.PathConstant;
import com.qu.app.service.PostVotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/post/vote/")
public class PostVotesController {
    @Autowired
    PostVotesService postVotesService;

    @PostMapping(PathConstant.VOTE_A_POST)
    public VoteAPostResponseDTO voteToAPost(@PathVariable(value = "title") String title, HttpServletRequest request) {
        return postVotesService.voteToAPost(title, request);
    }

    @PostMapping(PathConstant.GET_A_LIST_OF_USER_WHO_VOTED_THAT_POST)
    public List<GetAListOfUserWhoVotedThatPostDTO> getAListOfUserWhoVotedThatPost(@PathVariable(value = "postTitle") String postTitle) {
        return postVotesService.getAListOfUserWhoVotedThatPostService(postTitle);
    }

    @PostMapping(PathConstant.CURRENT_USER_VOTED_POST_LIST)
    public List<GetAPostDTO> getCurrentUserVotedPostList(@RequestHeader("Authorization") String token) {
        return postVotesService.getCurrentUserVotedPostList(token);
    }

}