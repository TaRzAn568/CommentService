package com.example.Comment_Service.services.impl;

import com.example.Comment_Service.dto.CommentDto;
import com.example.Comment_Service.exception.ResourceNotFoundException;
import com.example.Comment_Service.model.*;
import com.example.Comment_Service.repository.*;
import com.example.Comment_Service.services.CommentService;
import org.modelmapper.ModelMapper;
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
    private ParentChildCommentRepository parentChildCommentRepository;

    @Autowired
    ModelMapper modelMapper;


    /*private CommentDto commentToDtoWithoutChild(Comment comment){
        CommentDto commentDto = modelMapper.addMappings(
                new PropertyMap<Comment, CommentDto>() {

                    @Override
                    protected void configure() {
                        skip().setChildComments(null);
                    }
                }
        ).map(comment);
        commentDto.setPost_Id(comment.getPost().getId());
        commentDto.setUser_Id(comment.getUser().getId());
        return commentDto;
    }*/
    public CommentDto commentToDto(Comment comment) {
        CommentDto commentDto = commentToDtoWithNoChild(comment);
        List<Comment> childComments = comment.getChildComments().stream().map(ParentChildComment::getChildComment).toList();
        List<CommentDto> childCommentDtos = new ArrayList<>();
        for(Comment child : childComments){
            childCommentDtos.add(commentToDtoWithNoChild(child));
        }
        commentDto.setChildComment(childCommentDtos);
        return commentDto;


    }

    private static CommentDto commentToDtoWithNoChild(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setUser_Id(comment.getUser().getId());
        commentDto.setPost_Id(comment.getPost().getId());
        commentDto.setLikes(comment.getLikes());
        commentDto.setDislikes(comment.getDislikes());
        return commentDto;
    }

    public Comment dtoToComment(CommentDto commentDto) {
        return modelMapper.map(commentDto, Comment.class);
    }



    @Override
    public List<CommentDto> getAllTopLevelComments(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        List<Comment> comments = commentRepository.findTopLevelComments(postId);
        return comments.stream().map(this::commentToDto).toList();
    }

    @Override
    public CommentDto getCommentById(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));
        return commentToDto(comment);
    }

    @Override
    public CommentDto addCommentToPost(CommentDto commentDto, Long postId) {
        User user = userRepository.findById(commentDto.getUser_Id()).orElseThrow(() -> new ResourceNotFoundException("User", "id", commentDto.getUser_Id()));
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", commentDto.getPost_Id()));

        Comment comment = dtoToComment(commentDto);
        comment.setPost(post);
        comment.setUser(user);
        Comment savedComment = commentRepository.save(comment);
        return commentToDto(savedComment);
    }

    @Override
    public CommentDto updateComment(Long id, CommentDto newCommentDto) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));
        Comment newComment = dtoToComment(newCommentDto);
        comment.setText(newComment.getText());
        Comment savedComment = commentRepository.save(comment);
        return commentToDto(savedComment);
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

        Comment childComment = dtoToComment(commentDto);
        childComment.setUser(user);
        childComment.setPost(parentComment.getPost());

        ParentChildComment parentChildComment = new ParentChildComment();
        parentChildComment.setParentComment(parentComment);
        parentChildComment.setChildComment(childComment);

        CommentDto savedCommentDto =  commentToDto(commentRepository.save(childComment));
        parentChildCommentRepository.save(parentChildComment);
        return savedCommentDto;
    }



    @Override
    public List<CommentDto> getRepliesToComment(Long parentCommentId) {
        Comment parentComment = commentRepository.findById(parentCommentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", parentCommentId));
        List<Comment> replies = commentRepository.findAllChildComment(parentCommentId);

        return replies.stream().map(this::commentToDto).collect(Collectors.toList());
    }
}
