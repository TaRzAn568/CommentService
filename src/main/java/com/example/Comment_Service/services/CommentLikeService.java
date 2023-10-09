package com.example.Comment_Service.services;

import com.example.Comment_Service.ENUM.LikeStatus;
import com.example.Comment_Service.dto.*;
import com.example.Comment_Service.model.Comment;
import com.example.Comment_Service.model.User;

import java.util.List;


public interface CommentLikeService {

     ApiResponse<LikeDislikeDto> likeOrDisLikeComment(LikeDislikeDto likeDislikeDto, LikeStatus likeOrDislike);

     ApiResponse<Object> removeLikeOrDislikeOnComment(Long commentId, Long userId);

     List<UserDto> getLikesOnComment(Long commentId);

     List<UserDto> getDislikesOnComment(Long commentId);

     List<CommentDto> getLikedCommentsByUser(Long userId);

     List<CommentDto> getDislikedCommentsByUser(Long userId);

     boolean hasUserLikedOrDislikedComment(Comment comment, User user, LikeStatus likeStatus) ;



}