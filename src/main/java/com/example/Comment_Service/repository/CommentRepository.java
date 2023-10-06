package com.example.Comment_Service.repository;

import com.example.Comment_Service.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByUserId(Long userId);
    @Query("SELECT c FROM Comment c WHERE c.id NOT IN (SELECT DISTINCT pc.childComment.id " +
            "FROM ParentChildComment pc WHERE pc.parentComment.id = c.id) AND c.post.id = :postId")
    List<Comment> findTopLevelComments(@Param("postId") Long postId);


    @Query("SELECT c FROM Comment c WHERE c.id IN (SELECT DISTINCT pc.childComment.id FROM " +
            "ParentChildComment pc WHERE pc.parentComment.id = :parentCommentId) AND c.id != :parentCommentId")
    List<Comment> findAllChildComment(@Param("parentCommentId") Long parentCommentId);

}
