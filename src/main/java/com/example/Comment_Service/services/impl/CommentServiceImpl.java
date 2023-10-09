package com.example.Comment_Service.services.impl;

import com.example.Comment_Service.ENUM.LikeStatus;
import com.example.Comment_Service.dto.CommentDto;
import com.example.Comment_Service.exception.ResourceNotFoundException;
import com.example.Comment_Service.mapping.CommentMapper;
import com.example.Comment_Service.model.*;
import com.example.Comment_Service.repository.*;
import com.example.Comment_Service.services.CommentService;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<CommentDto> getAllTopLevelComments(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        List<Comment> comments = commentRepository.findTopLevelComments(postId);
        return comments.stream().map(comment ->commentMapper.toDto(comment)).toList();
    }

    @Override
    public CommentDto getCommentById(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));
        return commentMapper.toDto(comment);
    }

    @Override
    public CommentDto addCommentToPost(CommentDto commentDto, Long postId) {
        User user = userRepository.findById(commentDto.getUser_Id()).orElseThrow(() -> new ResourceNotFoundException("User", "id", commentDto.getUser_Id()));
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", commentDto.getPost_Id()));

        Comment comment = commentMapper.toEntity(commentDto);
        comment.setPost(post);
        comment.setUser(user);
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
        commentRepository.delete(comment);
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
    public List<CommentDto> getRepliesToComment(Long parentCommentId) {
        Comment parentComment = commentRepository.findById(parentCommentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", parentCommentId));
        List<Comment> replies = commentRepository.findNextLevelComments(parentComment);

        return replies.stream().map(comment -> commentMapper.toDto(comment)).collect(Collectors.toList());
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
