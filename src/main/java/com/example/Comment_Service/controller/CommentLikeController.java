package com.example.Comment_Service.controller;

import com.example.Comment_Service.ENUM.LikeStatus;
import com.example.Comment_Service.dto.ApiResponse;
import com.example.Comment_Service.dto.CommentDto;
import com.example.Comment_Service.dto.LikeDislikeDto;
import com.example.Comment_Service.dto.UserDto;
import com.example.Comment_Service.services.CommentLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<Map<String, Object>> getLikesByComment(@PathVariable Long commentId,
                                                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                                                 @RequestParam(value = "size", defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDto> likes = commentLikeService.getLikesOnComment(commentId, pageable);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("Total Likes", likes.getTotalElements());
        responseMap.put("Users", likes);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping("/comment/{commentId}/dislikes")
    public ResponseEntity<Map<String, Object>> getDislikesByComment(@PathVariable Long commentId,
                                                                    @RequestParam(value = "page", defaultValue = "0") int page,
                                                                    @RequestParam(value = "size", defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDto> disLikes = commentLikeService.getDislikesOnComment(commentId, pageable);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("Total DisLikes", disLikes.getTotalElements());
        responseMap.put("Users", disLikes);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/liked-comments")
    public ResponseEntity<Page<CommentDto>> getLikedCommentsByUser(@PathVariable Long userId,
                                                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                                                   @RequestParam(value = "size", defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CommentDto> likedComments = commentLikeService.getLikedCommentsByUser(userId, pageable);
        return new ResponseEntity<>(likedComments, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/disliked-comments")
    public ResponseEntity<Page<CommentDto>> getDislikedCommentsByUser(@PathVariable Long userId,
                                                                      @RequestParam(value = "page", defaultValue = "0") int page,
                                                                      @RequestParam(value = "size", defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CommentDto> dislikedComments = commentLikeService.getDislikedCommentsByUser(userId, pageable);
        return new ResponseEntity<>(dislikedComments, HttpStatus.OK);
    }

}
