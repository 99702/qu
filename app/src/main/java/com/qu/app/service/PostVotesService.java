package com.qu.app.service;

import com.qu.app.dto.post.GetAPostDTO;
import com.qu.app.dto.postVotes.response.GetAListOfUserWhoVotedThatPostDTO;
import com.qu.app.dto.postVotes.response.VoteAPostResponseDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PostVotesService {

    /**
     * Toggle vote / unvote for that post title , taking user from token
     * @param title
     * @param request
     * @return VoteAPostResponseDTO
     */
    VoteAPostResponseDTO voteToAPost(String title, HttpServletRequest request);

    List<GetAListOfUserWhoVotedThatPostDTO> getAListOfUserWhoVotedThatPostService(String title);

    List<GetAPostDTO> getCurrentUserVotedPostList(String token);

}
