package com.example.Comment_Service.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.Comment_Service.dto.CommentDto;
import com.example.Comment_Service.entity.Comment;
import com.example.Comment_Service.entity.Post;
import com.example.Comment_Service.entity.User;
import com.example.Comment_Service.mapping.CommentMapper;

import com.example.Comment_Service.repository.CommentRepository;
import com.example.Comment_Service.repository.PostRepository;
import com.example.Comment_Service.repository.UserRepository;
import com.example.Comment_Service.services.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class CommentServiceImplTest {

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCommentById() {
        // Arrange
        Comment comment = new Comment();
        comment.setId(1L);

        //Expected
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(commentMapper.toDto(comment)).thenReturn(commentDto);

        // Actual
        CommentDto result = commentService.getCommentById(1L);


        assertNotNull(result);
        assertEquals(commentDto.getId(), result.getId());

    }

    @Test
    public void testAddCommentToPost() {
        // Arrange
        CommentDto commentDto = new CommentDto();
        commentDto.setUser_Id(1L);
        commentDto.setPost_Id(2L);

        User user = new User();
        user.setId(1L);

        Post post = new Post();
        post.setId(2L);

        Comment comment = new Comment();
        comment.setId(3L);
        comment.setText("Test Comment");

        CommentDto savedDto = new CommentDto();
        savedDto.setId(comment.getId());
        savedDto.setText(comment.getText());


        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(postRepository.findById(2L)).thenReturn(Optional.of(post));
        when(commentMapper.toEntity(commentDto)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.toDto(comment)).thenReturn(savedDto);

        // Act
        CommentDto result = commentService.addCommentToPost(commentDto, 2L);

        // Assert
        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("Test Comment", result.getText());
    }
}
