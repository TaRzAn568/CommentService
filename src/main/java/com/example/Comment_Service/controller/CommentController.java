package com.example.Comment_Service.controller;

import com.example.Comment_Service.dto.CommentDto;
import com.example.Comment_Service.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/comments")
public class CommentController {

    @Autowired
    CommentService commentService;
    @PostMapping("/add-comment/post/{postId}")
    public CommentDto addComment(@RequestBody CommentDto commentDto, @PathVariable Long postId) {
        return commentService.addComment(commentDto, postId);
    }

    @GetMapping("/top-level/{postId}")
    public List<CommentDto> getTopLevelComments(@PathVariable Long postId) {
        return commentService.getTopLevelComments(postId);
    }

    @GetMapping("/{parentCommentId}/children")
    public List<CommentDto> getChildComments(@PathVariable Long parentCommentId) {
        return commentService.getChildComments(parentCommentId);
    }
}
