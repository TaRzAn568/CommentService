package com.example.Comment_Service.controller;

import com.example.Comment_Service.dto.ApiResponse;
import com.example.Comment_Service.dto.CommentDto;
import com.example.Comment_Service.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/comments")
public class CommentController {

    @Autowired
    CommentService commentService;

    @PostMapping("/add/{postId}")
    public ResponseEntity<CommentDto> addComment(@RequestBody CommentDto commentDto, @PathVariable Long postId) {
        CommentDto responseDto = commentService.addCommentToPost(commentDto, postId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public CommentDto updateComment(@PathVariable Long id, @RequestBody CommentDto commentDto) {
        return commentService.updateComment(id, commentDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return new ResponseEntity<>(new ApiResponse<>("Comment deleted successfully", true, null), HttpStatus.OK);

    }

    @GetMapping("/topLevelComments/{postId}")
    public Page<CommentDto> getTopLevelComments(@PathVariable Long postId,
                                                @RequestParam(value = "page", defaultValue = "0") int page,
                                                @RequestParam(value = "size", defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return commentService.getAllTopLevelComments(postId,pageable);
    }

    @GetMapping("/{commentId}")
    public CommentDto getCommentById(@PathVariable Long commentId) {
        return commentService.getCommentById(commentId);
    }



    @PostMapping("/{comment_id}/reply")
    public ResponseEntity<CommentDto> addReplyOnComment(@PathVariable Long comment_id, @RequestBody CommentDto commentDto) {
        CommentDto responseDto = commentService.addReplyToComment(commentDto, comment_id);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/{parentCommentId}/replies")
    public Page<CommentDto> getAllReplies(@PathVariable Long parentCommentId,
                                          @RequestParam(value = "page", defaultValue = "0") int page,
                                          @RequestParam(value = "size", defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return commentService.getRepliesToComment(parentCommentId, pageable);
    }

    @GetMapping("user/{userId}/comments")
    public Page<CommentDto> getAllCommentsByUser(@PathVariable Long userId,
                                          @RequestParam(value = "page", defaultValue = "0") int page,
                                          @RequestParam(value = "size", defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return commentService.getAllCommentsByUser(userId, pageable);
    }



}
