package com.example.Comment_Service.entity;

import com.example.Comment_Service.ENUM.LikeStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "like_dislike")
public class LikeDislike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private LikeStatus status; // Enum for LIKE or DISLIKE

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    // Constructors, getters/setters
}
