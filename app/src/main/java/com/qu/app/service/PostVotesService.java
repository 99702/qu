package com.qu.app.service;

import com.qu.app.dto.postVotes.response.VoteAPostResponseDTO;

import javax.servlet.http.HttpServletRequest;

public interface PostVotesService {
    /**
     * Toggle vote / unvote for that post title , taking user from token
     * @param title
     * @param request
     * @return VoteAPostResponseDTO
     */
    VoteAPostResponseDTO voteToAPost(String title, HttpServletRequest request);
}
