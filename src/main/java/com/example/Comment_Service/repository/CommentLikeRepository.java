package com.example.Comment_Service.repository;

import com.example.Comment_Service.ENUM.LikeStatus;
import com.example.Comment_Service.dto.CommentDto;
import com.example.Comment_Service.model.Comment;
import com.example.Comment_Service.model.CommentLike;
import com.example.Comment_Service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    List<CommentLike> findByComment(Comment comment);
    List<CommentLike> findByUser(User user);

    List<CommentLike> findByCommentAndStatus(Comment comment, LikeStatus status);

    List<CommentLike> findByUserAndStatus(User user, LikeStatus status);

    boolean existsByCommentAndUserAndStatus(Comment comment, User user, LikeStatus status);

    CommentLike findByCommentAndUser(Comment comment, User user);



}
