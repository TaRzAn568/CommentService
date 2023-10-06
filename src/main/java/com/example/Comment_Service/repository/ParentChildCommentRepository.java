package com.example.Comment_Service.repository;

import com.example.Comment_Service.model.Comment;
import com.example.Comment_Service.model.ParentChildComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParentChildCommentRepository extends JpaRepository<ParentChildComment, Long> {

    List<ParentChildComment> findByParentComment(Comment parentComment);
    List<ParentChildComment> findByChildComment(Comment childComment);
}
