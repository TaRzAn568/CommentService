package com.example.Comment_Service.repository;

import com.example.Comment_Service.model.Comment;
import com.example.Comment_Service.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByParentComment(Long parentCommentId);
    List<Comment> findByPostAndParentCommentIsNull(Long postId);
}
