package com.example.Comment_Service.services;

import com.example.Comment_Service.ENUM.LikeStatus;
import com.example.Comment_Service.dto.*;
import com.example.Comment_Service.entity.Comment;
import com.example.Comment_Service.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface CommentLikeService {

     ApiResponse<LikeDislikeDto> likeOrDisLikeComment(LikeDislikeDto likeDislikeDto, LikeStatus likeOrDislike);

     ApiResponse<Object> removeLikeOrDislikeOnComment(Long commentId, Long userId);

     Page<UserDto> getLikesOnComment(Long commentId, Pageable pageable);

     Page<UserDto> getDislikesOnComment(Long commentId, Pageable pageable);

     Page<CommentDto> getLikedCommentsByUser(Long userId, Pageable pageable);

     Page<CommentDto> getDislikedCommentsByUser(Long userId, Pageable pageable);

     boolean hasUserLikedOrDislikedComment(Comment comment, User user, LikeStatus likeStatus) ;



}