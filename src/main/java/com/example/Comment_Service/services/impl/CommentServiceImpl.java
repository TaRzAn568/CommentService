package com.example.Comment_Service.services.impl;

import com.example.Comment_Service.ENUM.LikeStatus;
import com.example.Comment_Service.dto.CommentDto;
import com.example.Comment_Service.exception.ResourceNotFoundException;
import com.example.Comment_Service.mapping.CommentMapper;
import com.example.Comment_Service.entity.*;
import com.example.Comment_Service.repository.*;
import com.example.Comment_Service.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    CommentMapper commentMapper;


    @Override
    public CommentDto addCommentToPost(CommentDto commentDto, Long postId) {
        User user = userRepository.findById(commentDto.getUser_Id()).orElseThrow(() -> new ResourceNotFoundException("User", "id", commentDto.getUser_Id()));
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", commentDto.getPost_Id()));
        commentDto.setPost_Id(postId);
        Comment comment = commentMapper.toEntity(commentDto);
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toDto(savedComment);
    }

    @Override
    public CommentDto updateComment(Long id, CommentDto newCommentDto) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));
        Comment newComment = commentMapper.toEntity(newCommentDto);
        comment.setText(newComment.getText());
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toDto(savedComment);
    }

    @Override
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));
        // Delete child comments first
        deleteChildComments(comment);
        // Delete the parent comment
        commentRepository.delete(comment);

    }

    // Recursive method to collect comments to delete
    private void deleteChildComments(Comment comment) {
        for (Comment child : comment.getChildComments()) {
            deleteChildComments(child);
            commentRepository.delete(child);
        }
    }

    @Override
    public CommentDto addReplyToComment(CommentDto commentDto, Long parentCommentId) {
        User user = userRepository.findById(commentDto.getUser_Id()).orElseThrow(() -> new ResourceNotFoundException("User", "id", commentDto.getUser_Id()));
        Comment parentComment = commentRepository.findById(parentCommentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", parentCommentId));
        Comment childComment = commentMapper.toEntity(commentDto);
        childComment.setUser(user);
        childComment.setPost(parentComment.getPost());
        childComment.setParentCommentId(parentComment);
        return commentMapper.toDto(commentRepository.save(childComment));
    }

    @Override
    public Page<CommentDto> getAllTopLevelComments(Long postId, Pageable pageable) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        Page<Comment> comments = commentRepository.findTopLevelComments(postId, pageable);
        List<CommentDto> commentDtos = comments.getContent().stream().map(comment -> commentMapper.toDto(comment)).toList();
        return new PageImpl<>(commentDtos, pageable, comments.getTotalElements());
    }

    @Override
    public CommentDto getCommentById(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));
        return commentMapper.toDto(comment);
    }


    @Override
    public Page<CommentDto> getRepliesToComment(Long parentCommentId, Pageable pageable) {
        Comment parentComment = commentRepository.findById(parentCommentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", parentCommentId));
        Page<Comment> replies = commentRepository.findNextLevelComments(parentComment, pageable);
        List<CommentDto> commentDtos = replies.stream().map(comment -> commentMapper.toDto(comment)).toList();
        return new PageImpl<>(commentDtos, pageable, replies.getTotalElements());
    }

    @Override
    public Page<CommentDto> getAllCommentsByUser(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Page<Comment> comments = commentRepository.findByUserId(userId, pageable);
        List<CommentDto> commentDtos = comments.stream().map(comment -> commentMapper.toDto(comment)).toList();
        return new PageImpl<>(commentDtos, pageable, comments.getTotalElements());
    }

    public void incrementLikesOrDislikes(Comment comment, LikeStatus likeStatus) {
        if (likeStatus == LikeStatus.LIKE) {
            comment.setLikes(comment.getLikes() + 1);
        } else if (likeStatus == LikeStatus.DISLIKE) {
            comment.setDislikes(comment.getDislikes() + 1);
        }
        commentRepository.save(comment);
    }

    public void decrementLikesOrDislikes(Comment comment, LikeStatus likeStatus) {
        if (likeStatus == LikeStatus.LIKE && comment.getLikes() > 0) {
            comment.setLikes(comment.getLikes() - 1);
        } else if (likeStatus == LikeStatus.DISLIKE && comment.getDislikes() > 0) {
            comment.setDislikes(comment.getDislikes() - 1);
        }
        commentRepository.save(comment);
    }
}
