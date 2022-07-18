package com.qu.app.entity;

import javax.persistence.*;

@Entity
public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image", unique = false, length = 300000)
    private byte[] image;
}
