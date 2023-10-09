package com.example.Comment_Service.controller;

import com.example.Comment_Service.ENUM.LikeStatus;
import com.example.Comment_Service.dto.ApiResponse;
import com.example.Comment_Service.dto.CommentDto;
import com.example.Comment_Service.dto.LikeDislikeDto;
import com.example.Comment_Service.dto.UserDto;
import com.example.Comment_Service.services.CommentLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/comment-like/")
public class CommentLikeController {

    @Autowired
    private CommentLikeService commentLikeService;

    @PostMapping("/like")
    public ResponseEntity<ApiResponse<LikeDislikeDto>> likeComment(@RequestBody LikeDislikeDto likeDislikeDto) {
        // Assuming LikeRequest includes commentId, userId, and status
        likeDislikeDto.setStatus(LikeStatus.LIKE);
        ApiResponse<LikeDislikeDto> response = commentLikeService.likeOrDisLikeComment(likeDislikeDto, LikeStatus.LIKE);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/dislike")
    public ResponseEntity<ApiResponse<LikeDislikeDto>> dislikeComment(@RequestBody LikeDislikeDto likeDislikeDto) {
        // Assuming LikeRequest includes commentId, userId, and status
        likeDislikeDto.setStatus(LikeStatus.LIKE);
        ApiResponse<LikeDislikeDto> response = commentLikeService.likeOrDisLikeComment(likeDislikeDto, LikeStatus.DISLIKE);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/comment/{commentId}/user/{userId}")
    public ResponseEntity<ApiResponse<Object>> removeLikeOrDislike(@PathVariable Long commentId, @PathVariable Long userId) {
        return new ResponseEntity<>(commentLikeService.removeLikeOrDislikeOnComment(commentId, userId),HttpStatus.OK);
    }

    @GetMapping("/comment/{commentId}/likes")
    public ResponseEntity<Map<String, Object>> getLikesByComment(@PathVariable Long commentId) {
        List<UserDto> likes = commentLikeService.getLikesOnComment(commentId);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("Total Likes", likes.size());
        responseMap.put("Users", likes);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping("/comment/{commentId}/dislikes")
    public ResponseEntity<Map<String, Object>> getDislikesByComment(@PathVariable Long commentId) {
        List<UserDto> disLikes = commentLikeService.getDislikesOnComment(commentId);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("Total DisLikes", disLikes.size());
        responseMap.put("Users", disLikes);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/liked-comments")
    public ResponseEntity<List<CommentDto>> getLikedCommentsByUser(@PathVariable Long userId) {
        List<CommentDto> likedComments = commentLikeService.getLikedCommentsByUser(userId);
        return new ResponseEntity<>(likedComments, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/disliked-comments")
    public ResponseEntity<List<CommentDto>> getDislikedCommentsByUser(@PathVariable Long userId) {
        List<CommentDto> dislikedComments = commentLikeService.getDislikedCommentsByUser(userId);
        return new ResponseEntity<>(dislikedComments, HttpStatus.OK);
    }

}
