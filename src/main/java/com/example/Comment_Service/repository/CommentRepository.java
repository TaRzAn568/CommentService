package com.example.Comment_Service.repository;

import com.example.Comment_Service.model.Comment;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByUserId(Long userId);
    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId AND c.parentCommentId is null")
    List<Comment> findTopLevelComments(@Param("postId") Long postId);


    @Query("SELECT c FROM Comment AS c " +
            "WHERE c.id IN (Select c1.id from Comment c1 where c1.parentCommentId = :parentCommentId)")
    List<Comment> findNextLevelComments(@Param("parentCommentId") Comment parentCommentId);





}
