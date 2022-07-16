package com.qu.app.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(Comment.class)
@Data
public class Comment implements Serializable {
    @Column(name="comment")
    private String comment;

    @Id
    @ManyToOne()
    @JoinColumn(name = "FK_POST", referencedColumnName = "id", nullable = false)
    private Post post;

    @Id
    @ManyToOne()
    @JoinColumn(name = "FK_USER", nullable = false, referencedColumnName = "id")
    private User user;
}