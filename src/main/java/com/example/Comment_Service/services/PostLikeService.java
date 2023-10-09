package com.example.Comment_Service.services;

import com.example.Comment_Service.ENUM.LikeStatus;
import com.example.Comment_Service.dto.ApiResponse;
import com.example.Comment_Service.dto.LikeDislikeDto;
import com.example.Comment_Service.dto.PostDto;
import com.example.Comment_Service.dto.UserDto;
import com.example.Comment_Service.model.Post;
import com.example.Comment_Service.model.User;

import java.util.List;

public interface PostLikeService {

    ApiResponse<LikeDislikeDto> likeOrDisLikePost(LikeDislikeDto likeDislikeDto, LikeStatus likeOrDislike);

    ApiResponse<Object> removeLikeOrDislikeOnPost(Long postId, Long userId);

    List<UserDto> getLikesOnPost(Long postId);

    List<UserDto> getDislikesOnPost(Long postId);

    List<PostDto> getLikedPostsByUser(Long userId);

    List<PostDto> getDislikedPostsByUser(Long userId);


    boolean hasUserLikedOrDislikedPost(Post comment, User user, LikeStatus likeStatus) ;
}
