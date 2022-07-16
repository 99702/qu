package com.qu.app.dto.postVotes.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder

public class VoteAPostResponseDTO {
    private Long voteCount;
    private String postTitle;
}
