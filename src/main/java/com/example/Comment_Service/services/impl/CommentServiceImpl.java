package com.example.Comment_Service.services.impl;

import com.example.Comment_Service.dto.CommentDto;
import com.example.Comment_Service.exception.ResourceNotFoundException;
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
    ModelMapper modelMapper;


    public CommentDto commentToDto(Comment comment) {

    PropertyMap<Comment, CommentDto> propertyMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setPost_Id(source.getPost().getId());
                map().setUser_Id(source.getUser().getId());
            }
        };

    TypeMap<Comment, CommentDto> typeMap = modelMapper.getTypeMap(Comment.class, CommentDto.class);
    if (typeMap == null){
        modelMapper.addMappings(propertyMap);
    }
    return modelMapper.map(comment, CommentDto.class);




       /* // Create a custom TypeMap to handle child and parent comments
        TypeMap<Comment, CommentDto> typeMap = modelMapper.createTypeMap(Comment.class, CommentDto.class);

        // Configure custom mappings for childComment and parentComment

        typeMap.addMapping(src -> src.getChildComments().stream()
                .map(child -> modelMapper.map(child.getChildComment(), CommentDto.class))
                .collect(Collectors.toList()), CommentDto::setChildComment);

        typeMap.addMapping(src -> src.getParentComments().stream()
                .map(parent -> modelMapper.map(parent.getParentComment(), CommentDto.class))
                .collect(Collectors.toList()), CommentDto::setParentComment);

        // Perform the conversion
        return modelMapper.map(comment, CommentDto.class);*/

       /* TypeMap<Comment, CommentDto> typeMap = modelMapper.createTypeMap(Comment.class, CommentDto.class);
        typeMap.addMappings(mapper -> {
            mapper.map(src -> src.getChildComments().stream()
                    .map(child -> modelMapper.map(child.getChildComment(), CommentDto.class))
                    .collect(Collectors.toList()), CommentDto::setChildComment);
            mapper.map(src -> src.getParentComments().stream()
                    .map(parent -> modelMapper.map(parent.getParentComment(), CommentDto.class))
                    .collect(Collectors.toList()), CommentDto::setParentComment);
        });

        return modelMapper.map(comment,CommentDto.class);*/
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
        childComment.setParentCommentId(parentComment);


        return commentToDto(commentRepository.save(childComment));
    }



    @Override
    public List<CommentDto> getRepliesToComment(Long parentCommentId) {
        Comment parentComment = commentRepository.findById(parentCommentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", parentCommentId));
        List<Comment> replies = commentRepository.findNextLevelComments(parentComment);

        return replies.stream().map(this::commentToDto).collect(Collectors.toList());
    }
}
