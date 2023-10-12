package com.example.Comment_Service.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.Comment_Service.ENUM.LikeStatus;
import com.example.Comment_Service.dto.CommentDto;
import com.example.Comment_Service.entity.Comment;
import com.example.Comment_Service.entity.Post;
import com.example.Comment_Service.entity.User;
import com.example.Comment_Service.mapping.CommentMapper;

import com.example.Comment_Service.repository.CommentRepository;
import com.example.Comment_Service.repository.PostRepository;
import com.example.Comment_Service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
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

    @Test
    public void testGetAllTopLevelComments() {
        // Arrange
        Long postId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        // Create a list of comments for testing
        List<Comment> comments = new ArrayList<>();
        Comment comment1 = new Comment();
        comment1.setId(1L);
        Comment comment2 = new Comment();
        comment2.setId(2L);
        comments.add(comment1);
        comments.add(comment2);

        // Create a Page of comments
        Page<Comment> commentPage = new PageImpl<>(comments, pageable, comments.size());

        when(postRepository.findById(postId)).thenReturn(Optional.of(new Post()));
        when(commentRepository.findTopLevelComments(eq(postId), eq(pageable))).thenReturn(commentPage);
        when(commentMapper.toDto(comment1)).thenReturn(new CommentDto());
        when(commentMapper.toDto(comment2)).thenReturn(new CommentDto());

        // Act
        Page<CommentDto> result = commentService.getAllTopLevelComments(postId, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
    }


    @Test
    public void testGetRepliesToComment() {
        // Arrange
        Long parentCommentId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        Comment parentComment = new Comment();
        parentComment.setId(parentCommentId);

        List<Comment> replies = new ArrayList<>();
        Comment reply1 = new Comment();
        reply1.setId(1L);
        Comment reply2 = new Comment();
        reply2.setId(2L);
        replies.add(reply1);
        replies.add(reply2);

        Page<Comment> replyPage = new PageImpl<>(replies, pageable, replies.size());

        when(commentRepository.findById(parentCommentId)).thenReturn(Optional.of(parentComment));
        when(commentRepository.findNextLevelComments(parentComment, pageable)).thenReturn(replyPage);
        when(commentMapper.toDto(reply1)).thenReturn(new CommentDto());
        when(commentMapper.toDto(reply2)).thenReturn(new CommentDto());

        // Act
        Page<CommentDto> result = commentService.getRepliesToComment(parentCommentId, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
    }


    @Test
    public void testDeleteComment() {
        // Arrange
        Comment comment = new Comment();
        comment.setId(5L);

        when(commentRepository.findById(5L)).thenReturn(Optional.of(comment));

        // Act
        assertDoesNotThrow(() -> commentService.deleteComment(5L));

        // Assert
        verify(commentRepository).delete(comment);
    }


    @Test
    public void testIncrementLikesOrDislikes() {
        // Arrange
        Comment comment = new Comment();
        comment.setId(6L);
        comment.setLikes(10);
        LikeStatus likeStatus = LikeStatus.LIKE;

        when(commentRepository.save(comment)).thenReturn(comment);

        // Act
        assertDoesNotThrow(() -> commentService.incrementLikesOrDislikes(comment, likeStatus));

        // Assert
        assertEquals(11, comment.getLikes());
    }

    @Test
    public void testDecrementLikesOrDislikes() {
        // Arrange
        Comment comment = new Comment();
        comment.setId(7L);
        comment.setLikes(5);
        LikeStatus likeStatus = LikeStatus.LIKE;

        when(commentRepository.save(comment)).thenReturn(comment);

        // Act
        assertDoesNotThrow(() -> commentService.decrementLikesOrDislikes(comment, likeStatus));

        // Assert
        assertEquals(4, comment.getLikes());
    }

    @Test
    public void testUpdateComment() {
        // Arrange
        CommentDto updatedCommentDto = new CommentDto();
        updatedCommentDto.setId(1L);

        Comment existingComment = new Comment();
        existingComment.setId(1L);

        // Mock the commentMapper.toDto method


        Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.of(existingComment));
        Mockito.when(commentRepository.save(any(Comment.class))).thenReturn(existingComment);
        Mockito.when(commentMapper.toEntity(updatedCommentDto)).thenReturn(existingComment);
        Mockito.when(commentMapper.toDto(existingComment)).thenReturn(updatedCommentDto);

        // Act
        CommentDto result = commentService.updateComment(1L, updatedCommentDto);

        // Assert
        assertNotNull(result);
        assertEquals(updatedCommentDto.getId(), result.getId());
    }


}
