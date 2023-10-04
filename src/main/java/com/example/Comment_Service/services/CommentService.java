package com.example.Comment_Service.services;

import com.example.Comment_Service.dto.CommentDto;
import com.example.Comment_Service.dto.ReplyDto;

import java.util.List;

public interface CommentService {

     List<CommentDto> getAllTopLevelComments(Long postId);

     CommentDto getCommentById(Long id);

     CommentDto addComment(CommentDto comment);


     CommentDto updateComment(Long id, CommentDto updatedComment);

     void deleteComment(Long id);

     ReplyDto addReplyToComment(ReplyDto reply, Long parentCommentId);


     List<ReplyDto> getRepliesToComment(Long parentCommentId);


}
