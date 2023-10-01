package com.example.Comment_Service.services.impl;

import com.example.Comment_Service.dto.CommentDto;
import com.example.Comment_Service.exception.ResourceNotFoundException;
import com.example.Comment_Service.model.Comment;
import com.example.Comment_Service.model.Post;
import com.example.Comment_Service.repository.CommentRepository;
import com.example.Comment_Service.repository.PostRepository;
import com.example.Comment_Service.services.CommentService;
import com.example.Comment_Service.utils.ModelMapperConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    ModelMapperConfig modelMapperConfig;

    public CommentDto addComment(CommentDto commentDto, Long postId) {

        Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post", "id", postId));
        Comment comment = dtoToComment(commentDto);
        comment.setPost(post);
        return commentToDto(commentRepository.save(comment));
    }

    public List<CommentDto> getTopLevelComments(Long postId) {
        List<Comment> comments =  commentRepository.findByPostAndParentCommentIsNull(postId);
        return comments.stream().map(this::commentToDto).collect(Collectors.toList());
    }

    public List<CommentDto> getChildComments(Long parentCommentId) {
        List<Comment> comments =  commentRepository.findByParentComment(parentCommentId);
        return comments.stream().map(this::commentToDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto addReply(CommentDto reply, Long parentCommentId) {
        Comment parentComment = commentRepository.findById(parentCommentId).
                orElseThrow(()-> new ResourceNotFoundException("Comment", "id",parentCommentId));

        reply.setParentCommentId(parentComment.getId());
        Comment comment = commentRepository.save(dtoToComment(reply));
        return commentToDto(comment);
    }

    public CommentDto commentToDto(Comment comment){
        return modelMapperConfig.modelMapper().map(comment, CommentDto.class);
    }
    public Comment dtoToComment(CommentDto commentDto){
        return modelMapperConfig.modelMapper().map(commentDto, Comment.class);
    }



}
