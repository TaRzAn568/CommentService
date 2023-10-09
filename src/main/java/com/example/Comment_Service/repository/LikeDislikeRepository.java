package com.example.Comment_Service.repository;

import com.example.Comment_Service.ENUM.LikeStatus;
import com.example.Comment_Service.model.Comment;
import com.example.Comment_Service.model.LikeDislike;
import com.example.Comment_Service.model.Post;
import com.example.Comment_Service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeDislikeRepository extends JpaRepository<LikeDislike, Long> {



    List<LikeDislike> findByCommentAndStatus(Comment comment, LikeStatus status);
    List<LikeDislike> findByUserAndStatusAndPostIsNull(User user, LikeStatus status);


    LikeDislike findByCommentAndUser(Comment comment, User user);

    boolean existsByCommentAndUserAndStatus(Comment comment, User user, LikeStatus status);


    List<LikeDislike> findByPostAndStatus(Post post, LikeStatus status);
    List<LikeDislike> findByUserAndStatusAndCommentIsNull(User user, LikeStatus status);


    LikeDislike findByPostAndUser(Post post, User user);

    boolean existsByPostAndUserAndStatus(Post post, User user, LikeStatus status);




}
