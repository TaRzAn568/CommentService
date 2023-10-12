package com.example.Comment_Service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="title", length = 100, nullable = false)
    private String title;

    @Column(name = "content", length = 10000)
    private String content;

    private Date createDate;
    private int likes;
    private int dislikes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Comment> comments = new ArrayList<>();

    // To create relationships for users who have liked and disliked the post
    @ManyToMany
    @JoinTable(
            name = "like_dislike",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> likedUsers = new ArrayList<>();






}
