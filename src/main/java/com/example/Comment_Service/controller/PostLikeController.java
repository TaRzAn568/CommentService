package com.example.Comment_Service.controller;

import com.example.Comment_Service.ENUM.LikeStatus;
import com.example.Comment_Service.dto.*;
import com.example.Comment_Service.services.PostLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/post-like")
public class PostLikeController {

    @Autowired
    private PostLikeService postLikeService;

    @PostMapping("/like")
    public ResponseEntity<ApiResponse<LikeDislikeDto>> likePost(@RequestBody LikeDislikeDto likeDislikeDto) {
        // Assuming LikeRequest includes postId, userId, and status
        likeDislikeDto.setStatus(LikeStatus.LIKE);
        ApiResponse<LikeDislikeDto> response = postLikeService.likeOrDisLikePost(likeDislikeDto, LikeStatus.LIKE);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/dislike")
    public ResponseEntity<ApiResponse<LikeDislikeDto>> dislikeComment(@RequestBody LikeDislikeDto likeDislikeDto) {
        // Assuming LikeRequest includes commentId, userId, and status
        likeDislikeDto.setStatus(LikeStatus.LIKE);
        ApiResponse<LikeDislikeDto> response = postLikeService.likeOrDisLikePost(likeDislikeDto, LikeStatus.DISLIKE);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/post/{postId}/user/{userId}")
    public ResponseEntity<ApiResponse<Object>> removeLikeOrDislike(@PathVariable Long postId, @PathVariable Long userId) {
        return new ResponseEntity<>(postLikeService.removeLikeOrDislikeOnPost(postId, userId),HttpStatus.OK);
    }

    @GetMapping("/post/{postId}/likes")
    public ResponseEntity<Map<String, Object>> getLikesOnPost(@PathVariable Long postId) {
        List<UserDto> likes = postLikeService.getLikesOnPost(postId);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("Total Likes", likes.size());
        responseMap.put("Users", likes);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping("/post/{postId}/dislikes")
    public ResponseEntity<Map<String, Object>> getDislikesOnPost(@PathVariable Long postId) {
        List<UserDto> disLikes = postLikeService.getDislikesOnPost(postId);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("Total DisLikes", disLikes.size());
        responseMap.put("Users", disLikes);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/liked-posts")
    public ResponseEntity<List<PostDto>> getLikedCommentsByUser(@PathVariable Long userId) {
        List<PostDto> likedComments = postLikeService.getLikedPostsByUser(userId);
        return new ResponseEntity<>(likedComments, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/disliked-posts")
    public ResponseEntity<List<PostDto>> getDislikedCommentsByUser(@PathVariable Long userId) {
        List<PostDto> dislikedComments = postLikeService.getDislikedPostsByUser(userId);
        return new ResponseEntity<>(dislikedComments, HttpStatus.OK);
    }

}
