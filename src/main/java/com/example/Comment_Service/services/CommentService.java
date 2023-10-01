package com.example.Comment_Service.services;

import com.example.Comment_Service.dto.CommentDto;
import com.example.Comment_Service.model.Comment;
import com.example.Comment_Service.model.Post;

import java.util.List;

public interface CommentService {


     CommentDto addComment(CommentDto commentDto, Long postId) ;
     List<CommentDto> getTopLevelComments(Long postId) ;

     List<CommentDto> getChildComments(Long parentCommentId);

     CommentDto addReply(CommentDto reply, Long parentCommentId);


}
