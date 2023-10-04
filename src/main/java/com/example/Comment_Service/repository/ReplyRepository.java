package com.example.Comment_Service.repository;

import com.example.Comment_Service.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Query("SELECT r FROM Reply r WHERE r.parentComment.Id = :commentId")
    List<Reply> findRepliesByCommentId(@Param("commentId") Long parentCommentId);
}
