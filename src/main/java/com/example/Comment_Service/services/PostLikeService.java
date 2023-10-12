package com.example.Comment_Service.services;

import com.example.Comment_Service.ENUM.LikeStatus;
import com.example.Comment_Service.dto.ApiResponse;
import com.example.Comment_Service.dto.LikeDislikeDto;
import com.example.Comment_Service.dto.PostDto;
import com.example.Comment_Service.dto.UserDto;
import com.example.Comment_Service.entity.Post;
import com.example.Comment_Service.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostLikeService {

    ApiResponse<LikeDislikeDto> likeOrDisLikePost(LikeDislikeDto likeDislikeDto, LikeStatus likeOrDislike);

    ApiResponse<Object> removeLikeOrDislikeOnPost(Long postId, Long userId);

    Page<UserDto> getLikesOnPost(Long postId, Pageable pageable);

    Page<UserDto> getDislikesOnPost(Long postId, Pageable pageable);

    Page<PostDto> getLikedPostsByUser(Long userId, Pageable pageable);

    Page<PostDto> getDislikedPostsByUser(Long userId, Pageable pageable);


    boolean hasUserLikedOrDislikedPost(Post comment, User user, LikeStatus likeStatus) ;
}
