package com.qu.app.entity;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
@Data
public class PostVotesId implements Serializable {
    @ManyToOne
    @JoinColumn(name="fk_user",referencedColumnName = "id",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name="fk_post", referencedColumnName = "id", nullable = false)
    private Post post;
}
