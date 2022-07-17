package com.qu.app.dto.postVotes.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAListOfUserWhoVotedThatPostDTO {
    private String name;
    private String profilePic;
    private Long userId;
}
