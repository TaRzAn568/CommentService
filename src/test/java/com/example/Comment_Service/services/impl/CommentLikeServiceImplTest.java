package com.example.Comment_Service.services.impl;

import com.example.Comment_Service.ENUM.LikeStatus;
import com.example.Comment_Service.dto.ApiResponse;
import com.example.Comment_Service.dto.LikeDislikeDto;
import com.example.Comment_Service.entity.Comment;
import com.example.Comment_Service.entity.LikeDislike;
import com.example.Comment_Service.entity.User;
import com.example.Comment_Service.mapping.CommentMapper;
import com.example.Comment_Service.mapping.LikeDislikeMapper;
import com.example.Comment_Service.mapping.UserMapper;
import com.example.Comment_Service.repository.CommentRepository;
import com.example.Comment_Service.repository.LikeDislikeRepository;
import com.example.Comment_Service.repository.UserRepository;
import com.example.Comment_Service.services.CommentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CommentLikeServiceImplTest {

    @InjectMocks
    CommentLikeServiceImpl commentLikeService;

    @Mock
    private LikeDislikeRepository likeDislikeRepository;

    @Mock
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LikeDislikeMapper likeDislikeMapper;
    @Mock
    private UserMapper userMapper;

    @Mock
    private CommentMapper commentMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void likeOrDisLikeComment() {



        // Request
        LikeDislikeDto likeDislikeDto = new LikeDislikeDto();
        likeDislikeDto.setUserId(1L);
        likeDislikeDto.setStatus(LikeStatus.LIKE);
        likeDislikeDto.setCommentId(2L);

        // Setup
        User user = new User();
        user.setId(1L);

        Comment comment = new Comment();
        comment.setId(2L);
        comment.setText("Test Comment");

        LikeDislike likeDislike = new LikeDislike();
        likeDislike.setId(1L);
        likeDislike.setUser(user);
        likeDislike.setComment(comment);

        //Expected
        LikeDislikeDto savedDto = new LikeDislikeDto();
        savedDto.setId(likeDislike.getId());
        savedDto.setCommentId(comment.getId());
        savedDto.setUserId(user.getId());
        savedDto.setStatus(LikeStatus.LIKE);



        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(commentRepository.findById(likeDislikeDto.getCommentId())).thenReturn(Optional.of(comment));
        when(likeDislikeMapper.toEntity(likeDislikeDto)).thenReturn(likeDislike);
        when(likeDislikeRepository.save(likeDislike)).thenReturn(likeDislike);
        when(likeDislikeMapper.toDto(likeDislike)).thenReturn(savedDto);

        // Act
        ApiResponse<LikeDislikeDto> response = commentLikeService.likeOrDisLikeComment(likeDislikeDto, LikeStatus.LIKE);


        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getData().getId());
        assertEquals(2L, response.getData().getCommentId());
        assertEquals(LikeStatus.LIKE, response.getData().getStatus());
    }

    @Test
    void removeLikeOrDislikeOnComment() {
    }

    @Test
    void getLikesOnComment() {
    }

    @Test
    void getDislikesOnComment() {
    }

    @Test
    void getLikedCommentsByUser() {
    }

    @Test
    void getDislikedCommentsByUser() {
    }
}