package com.example.Comment_Service.services;

import com.example.Comment_Service.ENUM.LikeStatus;
import com.example.Comment_Service.dto.CommentDto;
import com.example.Comment_Service.dto.CommentLikeDto;
import com.example.Comment_Service.dto.UserDto;
import com.example.Comment_Service.model.Comment;
import com.example.Comment_Service.model.User;

import java.util.List;


public interface CommentLikeService {

     CommentLikeDto likeOrDisLikeComment(CommentLikeDto commentLikeDto, LikeStatus likeOrDislike);

     void removeLikeOrDislike(Long commentId, Long userId);

     List<UserDto> getLikesByComment(Long commentId);

     List<UserDto> getDislikesByComment(Long commentId);

     List<CommentDto> getLikedCommentsByUser(Long userId);

     List<CommentDto> getDislikedCommentsByUser(Long userId);

     boolean hasUserLikedOrDislikedComment(Comment comment, User user, LikeStatus likeStatus) ;





    // Implement methods for liking, disliking, and retrieving likes/dislikes
}