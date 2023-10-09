package com.example.Comment_Service.services.impl;

import com.example.Comment_Service.ENUM.LikeStatus;
import com.example.Comment_Service.dto.*;
import com.example.Comment_Service.exception.ResourceNotFoundException;
import com.example.Comment_Service.mapping.CommentMapper;
import com.example.Comment_Service.mapping.LikeDislikeMapper;
import com.example.Comment_Service.mapping.UserMapper;
import com.example.Comment_Service.model.Comment;
import com.example.Comment_Service.model.LikeDislike;
import com.example.Comment_Service.model.User;
import com.example.Comment_Service.repository.LikeDislikeRepository;
import com.example.Comment_Service.repository.CommentRepository;
import com.example.Comment_Service.repository.UserRepository;
import com.example.Comment_Service.services.CommentLikeService;
import com.example.Comment_Service.services.CommentService;
import com.example.Comment_Service.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentLikeServiceImpl implements CommentLikeService {
    @Autowired
    private LikeDislikeRepository likeDislikeRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeDislikeMapper likeDislikeMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentMapper commentMapper;

    public ApiResponse<LikeDislikeDto> likeOrDisLikeComment(LikeDislikeDto likeDislikeDto, LikeStatus likeOrDislike) {
        User user = userRepository.findById(likeDislikeDto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User", "Id", likeDislikeDto.getUserId()));
        Comment comment = commentRepository.findById(likeDislikeDto.getCommentId()).orElseThrow(() -> new ResourceNotFoundException("Comment", "Id", likeDislikeDto.getCommentId()));
        likeDislikeDto.setStatus(likeOrDislike);
        likeDislikeDto.setCommentId(comment.getId());
        likeDislikeDto.setUserId(user.getId());

        if (!hasUserLikedOrDislikedComment(comment, user, likeDislikeDto.getStatus())) {
            LikeStatus previousLikeOrDisLike = CommonUtils.getReverseStatus(likeDislikeDto.getStatus());
            if (hasUserLikedOrDislikedComment(comment, user, previousLikeOrDisLike)) {
                removeLikeOrDislikeOnComment(likeDislikeDto.getCommentId(), user.getId());
            }
            commentService.incrementLikesOrDislikes(comment, likeDislikeDto.getStatus());
            LikeDislike likeDisLike = likeDislikeMapper.toEntity(likeDislikeDto);

            return new ApiResponse<>("User " + user.getId() + " has given " + likeDisLike.getStatus() + " to this comment ", true, likeDislikeMapper.toDto(likeDislikeRepository.save(likeDisLike)));
        }
        return new ApiResponse<>("User " + user.getId() + " has already given " + likeDislikeDto.getStatus() + " to the comment with id " + likeDislikeDto.getCommentId(), true, null); // User has already liked/disliked this comment
    }


    public ApiResponse<Object> removeLikeOrDislikeOnComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "Id", commentId));
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        LikeDislike likeDisLike = likeDislikeRepository.findByCommentAndUser(comment, user);
        if (likeDisLike != null && hasUserLikedOrDislikedComment(comment, user, likeDisLike.getStatus())) {
            commentService.decrementLikesOrDislikes(likeDisLike.getComment(), likeDisLike.getStatus());
            likeDislikeRepository.delete(likeDisLike);
            return new ApiResponse<>(likeDisLike.getStatus() + " deleted on comment", true, null);
        }
        return new ApiResponse<>("No reaction exist on this comment by user " + user.getId(), true, null);
    }

    public List<UserDto> getLikesOnComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("User", "id", commentId));
        List<LikeDislike> likeDislikes = likeDislikeRepository.findByCommentAndStatus(comment, LikeStatus.LIKE);
        List<User> likedUser = likeDislikes.stream().map(LikeDislike::getUser).toList();
        return likedUser.stream().map(user -> userMapper.toDto(user)).collect(Collectors.toList());
    }

    public List<UserDto> getDislikesOnComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
        List<LikeDislike> likeDislikes = likeDislikeRepository.findByCommentAndStatus(comment, LikeStatus.DISLIKE);
        List<User> likedUser = likeDislikes.stream().map(LikeDislike::getUser).toList();
        return likedUser.stream().map(user -> userMapper.toDto(user)).collect(Collectors.toList());
    }

    public List<CommentDto> getLikedCommentsByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        List<LikeDislike> commentsLikes = likeDislikeRepository.findByUserAndStatusAndPostIsNull(user, LikeStatus.LIKE);
        List<Comment> likedComments = commentsLikes.stream().map(LikeDislike::getComment).toList();
        return likedComments.stream().map(comment -> commentMapper.toDto(comment)).collect(Collectors.toList());
    }

    public List<CommentDto> getDislikedCommentsByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        List<LikeDislike> commentsLikes = likeDislikeRepository.findByUserAndStatusAndCommentIsNull(user, LikeStatus.DISLIKE);
        List<Comment> likedComments = commentsLikes.stream().map(LikeDislike::getComment).toList();
        return likedComments.stream().map(comment -> commentMapper.toDto(comment)).collect(Collectors.toList());
    }

    public boolean hasUserLikedOrDislikedComment(Comment comment, User user, LikeStatus likeStatus) {
        return likeDislikeRepository.existsByCommentAndUserAndStatus(comment, user, likeStatus);
    }
}

