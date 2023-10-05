package com.example.Comment_Service.controller;

import com.example.Comment_Service.dto.ApiResponse;
import com.example.Comment_Service.dto.CommentDto;
import com.example.Comment_Service.dto.ReplyDto;
import com.example.Comment_Service.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/comments")
public class CommentController {

    @Autowired
    CommentService commentService;
    @PostMapping("/add")
    public ResponseEntity<CommentDto> addComment(@RequestBody CommentDto commentDto) {
        CommentDto responseDto =  commentService.addComment(commentDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public CommentDto updateComment(@PathVariable Long id, @RequestBody CommentDto commentDto) {
        return commentService.updateComment(id, commentDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable Long id) {
         commentService.deleteComment(id);
        return new ResponseEntity<>(new ApiResponse("Comment deleted successfully", true), HttpStatus.OK);

    }

    @GetMapping("/topLevelComments/{postId}")
    public List<CommentDto> getTopLevelComments(@PathVariable Long postId) {
        return commentService.getAllTopLevelComments(postId);
    }

    @GetMapping("/{parentCommentId}/replies")
    public List<ReplyDto> getAllReplies(@PathVariable Long parentCommentId) {
        return commentService.getRepliesToComment(parentCommentId);
    }

    @PostMapping("/{comment_id}/reply")
    public ResponseEntity<ReplyDto> addReplyOnComment(@PathVariable Long comment_id, @RequestBody ReplyDto replyDto) {
        ReplyDto responseDto =  commentService.addReplyToComment(replyDto, comment_id);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
    @PostMapping("/{reply_id}/reply")
    public ResponseEntity<ReplyDto> addReplyOnReply(@PathVariable Long reply_id, @RequestBody ReplyDto replyDto) {
        ReplyDto responseDto =  commentService.addReplyToReply(replyDto, reply_id);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


}
