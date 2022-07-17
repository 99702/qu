package com.qu.app.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data

@Table(name = "rsa_keys")
public class Keys {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Lob
    private String RSAKey;

//    @CreatedDate
//    @Column(updatable = false)
//    private Date createdAt;


}
