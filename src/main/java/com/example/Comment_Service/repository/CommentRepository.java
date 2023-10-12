package com.example.Comment_Service.repository;

import com.example.Comment_Service.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByUserId(Long userId, Pageable pageable);
    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId AND c.parentCommentId is null")
    Page<Comment> findTopLevelComments(@Param("postId") Long postId, Pageable pageable);


    @Query("SELECT c FROM Comment AS c " +
            "WHERE c.id IN (Select c1.id from Comment c1 where c1.parentCommentId = :parentCommentId)")
    Page<Comment> findNextLevelComments(@Param("parentCommentId") Comment parentCommentId, Pageable pageable);





}
