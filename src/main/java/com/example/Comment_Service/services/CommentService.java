package com.example.Comment_Service.services;

import com.example.Comment_Service.dto.CommentDto;

import java.util.List;

public interface CommentService {

     List<CommentDto> getAllTopLevelComments(Long postId);

     CommentDto getCommentById(Long id);

     CommentDto addCommentToPost(CommentDto comment, Long postId);


     CommentDto updateComment(Long id, CommentDto updatedComment);

     void deleteComment(Long id);

     CommentDto addReplyToComment(CommentDto reply, Long parentCommentId);



     List<CommentDto> getRepliesToComment(Long parentCommentId);


}
