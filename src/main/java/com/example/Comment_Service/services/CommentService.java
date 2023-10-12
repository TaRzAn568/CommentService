package com.example.Comment_Service.services;

import com.example.Comment_Service.ENUM.LikeStatus;
import com.example.Comment_Service.dto.CommentDto;
import com.example.Comment_Service.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

     Page<CommentDto> getAllTopLevelComments(Long postId, Pageable pageable);

     CommentDto getCommentById(Long id);

     CommentDto addCommentToPost(CommentDto comment, Long postId);


     CommentDto updateComment(Long id, CommentDto updatedComment);

     void deleteComment(Long id);

     CommentDto addReplyToComment(CommentDto reply, Long parentCommentId);



     Page<CommentDto> getRepliesToComment(Long parentCommentId, Pageable pageable);
     Page<CommentDto> getAllCommentsByUser(Long userId, Pageable pageable);

      void incrementLikesOrDislikes(Comment comment, LikeStatus likeStatus);

      void decrementLikesOrDislikes(Comment comment, LikeStatus likeStatus);


}
