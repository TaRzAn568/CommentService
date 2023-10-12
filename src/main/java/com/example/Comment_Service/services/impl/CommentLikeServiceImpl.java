package com.example.Comment_Service.services.impl;

import com.example.Comment_Service.ENUM.LikeStatus;
import com.example.Comment_Service.dto.*;
import com.example.Comment_Service.exception.ResourceNotFoundException;
import com.example.Comment_Service.mapping.CommentMapper;
import com.example.Comment_Service.mapping.LikeDislikeMapper;
import com.example.Comment_Service.mapping.UserMapper;
import com.example.Comment_Service.entity.Comment;
import com.example.Comment_Service.entity.LikeDislike;
import com.example.Comment_Service.entity.User;
import com.example.Comment_Service.repository.LikeDislikeRepository;
import com.example.Comment_Service.repository.CommentRepository;
import com.example.Comment_Service.repository.UserRepository;
import com.example.Comment_Service.services.CommentLikeService;
import com.example.Comment_Service.services.CommentService;
import com.example.Comment_Service.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    public Page<UserDto> getLikesOnComment(Long commentId, Pageable pageable) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("User", "id", commentId));
        Page<LikeDislike> likeDislikes = likeDislikeRepository.findByCommentAndStatus(comment, LikeStatus.LIKE, pageable);
        List<User> likedUser = likeDislikes.getContent().stream().map(LikeDislike::getUser).toList();
        List<UserDto> likedUserDtos = likedUser.stream().map(user -> userMapper.toDto(user)).toList();
        return new PageImpl<>(likedUserDtos, pageable, likedUser.size());
    }

    public Page<UserDto> getDislikesOnComment(Long commentId, Pageable pageable) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
        Page<LikeDislike> likeDislikes = likeDislikeRepository.findByCommentAndStatus(comment, LikeStatus.DISLIKE, pageable);
        List<User> dislikedUser = likeDislikes.getContent().stream().map(LikeDislike::getUser).toList();
        List<UserDto> dislikedUserDtos = dislikedUser.stream().map(user -> userMapper.toDto(user)).toList();
        return new PageImpl<>(dislikedUserDtos, pageable, dislikedUser.size());
    }

    public Page<CommentDto> getLikedCommentsByUser(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Page<LikeDislike> commentsLikes = likeDislikeRepository.findByUserAndStatusAndPostIsNull(user, LikeStatus.LIKE, pageable);
        List<Comment> likedComments = commentsLikes.getContent().stream().map(LikeDislike::getComment).toList();
        List<CommentDto> likedCommentDtos = likedComments.stream().map(comment -> commentMapper.toDto(comment)).toList();
        return new PageImpl<>(likedCommentDtos, pageable, likedComments.size());
    }

    public Page<CommentDto> getDislikedCommentsByUser(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Page<LikeDislike> commentsLikes = likeDislikeRepository.findByUserAndStatusAndCommentIsNull(user, LikeStatus.DISLIKE, pageable);
        List<Comment> dislikedComments = commentsLikes.getContent().stream().map(LikeDislike::getComment).toList();
        List<CommentDto> dislikedCommentDtos = dislikedComments.stream().map(comment -> commentMapper.toDto(comment)).toList();
        return new PageImpl<>(dislikedCommentDtos, pageable, dislikedComments.size());
    }

    public boolean hasUserLikedOrDislikedComment(Comment comment, User user, LikeStatus likeStatus) {
        return likeDislikeRepository.existsByCommentAndUserAndStatus(comment, user, likeStatus);
    }
}

