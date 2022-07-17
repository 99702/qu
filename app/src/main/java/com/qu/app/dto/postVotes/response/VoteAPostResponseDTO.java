package com.qu.app.dto.postVotes.response;

import lombok.Data;

@Data
public class VoteAPostResponseDTO {
    private String votedBy;
    private Long voteCount;
    private String postTitle;
    private String message;
}
