package com.qu.app.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="dob", nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dob;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="mobile", nullable = false, unique = true)
    private String mobile;

    @Column(name = "image", unique = false, length = 100000)
    private byte[] profilePic;

    @Column(name="enabled")
    private Boolean enabled = false;

    @Column(name="role")
    private String role;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<Post> post = new HashSet<>();

    @OneToMany(fetch=FetchType.LAZY)
    @JoinColumn(name="FK_COMMENT", referencedColumnName = "id")
    private Set<Comment> comment = new HashSet<>();
}