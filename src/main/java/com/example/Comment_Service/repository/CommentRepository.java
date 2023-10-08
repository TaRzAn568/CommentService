package com.example.Comment_Service.repository;

import com.example.Comment_Service.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByUserId(Long userId);
    @Query("SELECT c FROM Comment c WHERE c.id NOT IN (SELECT DISTINCT pc.childComment.id " +
            "FROM ParentChildComment pc ) AND c.post.id = :postId")
    List<Comment> findTopLevelComments(@Param("postId") Long postId);


    @Query("SELECT c FROM Comment AS c " +
            "JOIN ParentChildComment AS pc ON c.id = pc.childComment.id " +
            "WHERE pc.parentComment.id = :parentCommentId")
    List<Comment> findAllChildComment(@Param("parentCommentId") Long parentCommentId);
     @Query("SELECT depth FROM ParentChildComment AS pc " +
            "WHERE pc.childComment.id = :commentId")
    Optional<Integer> findDepthOfComment(@Param("commentId") Long commentId);




}
