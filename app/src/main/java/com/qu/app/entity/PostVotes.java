package com.qu.app.entity;

import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
@Data
public class PostVotes {
    @EmbeddedId
    private PostVotesId postVotesId;

}