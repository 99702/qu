package com.qu.app.entity;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
public class PostVotes {
    @EmbeddedId
    private PostVotesId postVotesId;

}