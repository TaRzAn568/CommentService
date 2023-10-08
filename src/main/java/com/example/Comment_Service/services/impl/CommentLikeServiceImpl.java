package com.example.Comment_Service.services.impl;

import com.example.Comment_Service.ENUM.LikeStatus;
import com.example.Comment_Service.dto.CommentDto;
import com.example.Comment_Service.dto.CommentLikeDto;
import com.example.Comment_Service.dto.UserDto;
import com.example.Comment_Service.exception.ResourceNotFoundException;
import com.example.Comment_Service.model.Comment;
import com.example.Comment_Service.model.CommentLike;
import com.example.Comment_Service.model.User;
import com.example.Comment_Service.repository.CommentLikeRepository;
import com.example.Comment_Service.repository.CommentRepository;
import com.example.Comment_Service.repository.UserRepository;
import com.example.Comment_Service.services.CommentLikeService;
import com.example.Comment_Service.services.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentLikeServiceImpl implements CommentLikeService {
    @Autowired
    private CommentLikeRepository commentLikeRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;


    CommentLikeDto commentLikeToDto(CommentLike commentLike) {
        CommentLikeDto commentLikeDto = new CommentLikeDto();
        commentLikeDto.setId(commentLike.getId());
        commentLikeDto.setCommentId(commentLike.getComment().getId());
        commentLikeDto.setUserId(commentLike.getUser().getId());
        commentLikeDto.setStatus(commentLike.getStatus());

        return commentLikeDto;
    }

    CommentLike dtoToCommentLike(CommentLikeDto commentLikeDto, User user, Comment comment) {
        CommentLike commentLike = new CommentLike();
        commentLike.setId(commentLikeDto.getId());
        commentLike.setComment(comment);
        commentLike.setUser(user);
        commentLike.setStatus(commentLikeDto.getStatus());

        return commentLike;
    }

    public CommentLikeDto likeOrDisLikeComment(CommentLikeDto commentLikeDto, LikeStatus likeOrDislike) {
        User user = userRepository.findById(commentLikeDto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User", "Id", commentLikeDto.getUserId()));
        Comment comment = commentRepository.findById(commentLikeDto.getCommentId()).orElseThrow(() -> new ResourceNotFoundException("Comment", "Id", commentLikeDto.getCommentId()));
        commentLikeDto.setStatus(likeOrDislike);
        CommentLike commentLike = dtoToCommentLike(commentLikeDto, user, comment);
        if (!hasUserLikedOrDislikedComment(comment, user, commentLikeDto.getStatus())) {
            LikeStatus previousLikeOrDisLike = getReverseStatus(commentLikeDto.getStatus());
            if (hasUserLikedOrDislikedComment(comment, user, previousLikeOrDisLike)) {
                removeLikeOrDislike(commentLikeDto.getCommentId(), user.getId());
            }
            commentService.incrementLikesOrDislikes(comment, commentLikeDto.getStatus());
            return commentLikeToDto(commentLikeRepository.save(commentLike));
        }
        return null; // User has already liked/disliked this comment
    }

    private LikeStatus getReverseStatus(LikeStatus status) {
        if (status == LikeStatus.LIKE) {
            return LikeStatus.DISLIKE;
        } else {
            return LikeStatus.LIKE;
        }
    }


    public void removeLikeOrDislike(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "Id", commentId));
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        CommentLike commentLike = commentLikeRepository.findByCommentAndUser(comment, user);
        if (hasUserLikedOrDislikedComment(comment, user, commentLike.getStatus())) {
            commentService.decrementLikesOrDislikes(commentLike.getComment(), commentLike.getStatus());
            commentLikeRepository.delete(commentLike);
        }
    }

    public List<UserDto> getLikesByComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("User", "id", commentId));
        List<CommentLike> commentLikes = commentLikeRepository.findByCommentAndStatus(comment, LikeStatus.LIKE);
        List<User> likedUser = commentLikes.stream().map(CommentLike::getUser).toList();
        return likedUser.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
    }

    public List<UserDto> getDislikesByComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
        List<CommentLike> commentLikes = commentLikeRepository.findByCommentAndStatus(comment, LikeStatus.DISLIKE);
        List<User> likedUser = commentLikes.stream().map(CommentLike::getUser).toList();
        return likedUser.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
    }

    public List<CommentDto> getLikedCommentsByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        List<CommentLike> commentsLikes = commentLikeRepository.findByUserAndStatus(user, LikeStatus.LIKE);
        List<Comment> likedComments = commentsLikes.stream().map(CommentLike::getComment).toList();
        return likedComments.stream().map(comment -> modelMapper.map(comment, CommentDto.class)).collect(Collectors.toList());
    }

    public List<CommentDto> getDislikedCommentsByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        List<CommentLike> commentsLikes = commentLikeRepository.findByUserAndStatus(user, LikeStatus.DISLIKE);
        List<Comment> likedComments = commentsLikes.stream().map(CommentLike::getComment).toList();
        return likedComments.stream().map(comment -> modelMapper.map(comment, CommentDto.class)).collect(Collectors.toList());
    }

    public boolean hasUserLikedOrDislikedComment(Comment comment, User user, LikeStatus likeStatus) {
        return commentLikeRepository.existsByCommentAndUserAndStatus(comment, user, likeStatus);
    }
}

