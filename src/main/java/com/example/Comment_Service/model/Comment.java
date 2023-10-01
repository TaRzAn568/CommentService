package com.example.Comment_Service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comment")

public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String content;
    Date createDate;
    @Column(nullable = false)
    User user;
    @Column(nullable = false)
    Post post;
    @Column(name = "parent_comment_id", nullable = true)
    Comment parentComment;
}
