package com.example.Comment_Service.repository;

import com.example.Comment_Service.ENUM.LikeStatus;
import com.example.Comment_Service.entity.Comment;
import com.example.Comment_Service.entity.LikeDislike;
import com.example.Comment_Service.entity.Post;
import com.example.Comment_Service.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.*;
import java.util.List;

public interface LikeDislikeRepository extends JpaRepository<LikeDislike, Long> {



    Page<LikeDislike> findByCommentAndStatus(Comment comment, LikeStatus status, Pageable pageable);
    Page<LikeDislike> findByUserAndStatusAndPostIsNull(User user, LikeStatus status, Pageable pageable);


    LikeDislike findByCommentAndUser(Comment comment, User user);

    boolean existsByCommentAndUserAndStatus(Comment comment, User user, LikeStatus status);


    Page<LikeDislike> findByPostAndStatus(Post post, LikeStatus status, Pageable pageable);
    Page<LikeDislike> findByUserAndStatusAndCommentIsNull(User user, LikeStatus status, Pageable pageable);


    LikeDislike findByPostAndUser(Post post, User user);

    boolean existsByPostAndUserAndStatus(Post post, User user, LikeStatus status);




}
