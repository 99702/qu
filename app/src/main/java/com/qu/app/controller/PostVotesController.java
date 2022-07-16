package com.qu.app.controller;

import com.qu.app.dto.postVotes.response.VoteAPostResponseDTO;
import com.qu.app.enumeration.PathConstant;
import com.qu.app.service.PostVotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/post/vote/")
public class PostVotesController {
    @Autowired
    PostVotesService postVotesService;

    @PostMapping(PathConstant.VOTE_A_POST)
    public VoteAPostResponseDTO voteToAPost(@PathVariable(value = "title") String title, HttpServletRequest request){
        return postVotesService.voteToAPost(title, request);};
}