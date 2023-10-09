package com.example.Comment_Service.services.impl;

import com.example.Comment_Service.ENUM.LikeStatus;
import com.example.Comment_Service.dto.ApiResponse;
import com.example.Comment_Service.dto.LikeDislikeDto;
import com.example.Comment_Service.dto.PostDto;
import com.example.Comment_Service.dto.UserDto;
import com.example.Comment_Service.exception.ResourceNotFoundException;
import com.example.Comment_Service.mapping.PostMapper;
import com.example.Comment_Service.model.LikeDislike;
import com.example.Comment_Service.model.Post;
import com.example.Comment_Service.model.User;
import com.example.Comment_Service.repository.LikeDislikeRepository;
import com.example.Comment_Service.repository.PostRepository;
import com.example.Comment_Service.repository.UserRepository;
import com.example.Comment_Service.services.PostLikeService;
import com.example.Comment_Service.services.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.Comment_Service.utils.CommonUtils.getReverseStatus;

@Service
public class PostLikeServiceImpl implements PostLikeService {

    @Autowired
    private LikeDislikeRepository likeDislikeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PostMapper postMapper;

    public ApiResponse<LikeDislikeDto> likeOrDisLikePost(LikeDislikeDto likeDislikeDto, LikeStatus likeOrDislike) {
        User user = userRepository.findById(likeDislikeDto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User", "Id", likeDislikeDto.getUserId()));
        Post post = postRepository.findById(likeDislikeDto.getPostId()).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", likeDislikeDto.getPostId()));
        likeDislikeDto.setStatus(likeOrDislike);
        LikeDislike likeDisLike = dtoToLikeDislike(likeDislikeDto, user, post);
        if (!hasUserLikedOrDislikedPost(post, user, likeDislikeDto.getStatus())) {
            LikeStatus previousLikeOrDisLike = getReverseStatus(likeDislikeDto.getStatus());
            if (hasUserLikedOrDislikedPost(post, user, previousLikeOrDisLike)) {
                removeLikeOrDislikeOnPost(likeDislikeDto.getPostId(), user.getId());
            }
            postService.incrementLikesOrDislikes(post, likeDislikeDto.getStatus());
            return new ApiResponse<>("User "+user.getId()+ " has given "+ likeDisLike.getStatus() +" to this post ", true, likeDislikeToDto(likeDislikeRepository.save(likeDisLike)));
        }
        return new ApiResponse<>("User "+user.getId()+ " has already given "+ likeDisLike.getStatus() +" to the post with id " + likeDisLike.getPost().getId(), true, null); // User has already liked/disliked this post
    }




    public ApiResponse<Object> removeLikeOrDislikeOnPost(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", postId));
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        LikeDislike likeDisLike = likeDislikeRepository.findByPostAndUser(post, user);
        if (likeDisLike!=null && hasUserLikedOrDislikedPost(post, user, likeDisLike.getStatus())) {
            postService.decrementLikesOrDislikes(likeDisLike.getPost(), likeDisLike.getStatus());
            likeDislikeRepository.delete(likeDisLike);
            return new ApiResponse<>(likeDisLike.getStatus()+" deleted on post", true, null);
        }
        return new ApiResponse<>("No reaction exist on this post by user "+user.getId(), true, null);
    }

    public List<UserDto> getLikesOnPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        List<LikeDislike> likeDislikes = likeDislikeRepository.findByPostAndStatus(post, LikeStatus.LIKE);
        List<User> likedUser = likeDislikes.stream().map(LikeDislike::getUser).toList();
        return likedUser.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
    }

    public List<UserDto> getDislikesOnPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        List<LikeDislike> likeDislikes = likeDislikeRepository.findByPostAndStatus(post, LikeStatus.DISLIKE);
        List<User> likedUser = likeDislikes.stream().map(LikeDislike::getUser).toList();
        return likedUser.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
    }

    public List<PostDto> getLikedPostsByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        List<LikeDislike> postLikes = likeDislikeRepository.findByUserAndStatusAndCommentIsNull(user, LikeStatus.LIKE);
        List<Post> likedPosts = postLikes.stream().map(LikeDislike::getPost).toList();
        return likedPosts.stream().map(post -> postMapper.toDto(post)).collect(Collectors.toList());
    }

    public List<PostDto> getDislikedPostsByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        List<LikeDislike> postLikes = likeDislikeRepository.findByUserAndStatusAndCommentIsNull(user, LikeStatus.DISLIKE);
        List<Post> likedPosts = postLikes.stream().map(LikeDislike::getPost).toList();
        return likedPosts.stream().map(post -> postMapper.toDto(post)).collect(Collectors.toList());
    }

    public boolean hasUserLikedOrDislikedPost(Post post, User user, LikeStatus likeStatus) {
        return likeDislikeRepository.existsByPostAndUserAndStatus(post, user, likeStatus);
    }

    LikeDislikeDto likeDislikeToDto(LikeDislike likeDisLike) {
        LikeDislikeDto likeDislikeDto = new LikeDislikeDto();
        likeDislikeDto.setId(likeDisLike.getId());
        likeDislikeDto.setPostId(likeDisLike.getPost().getId());
        likeDislikeDto.setUserId(likeDisLike.getUser().getId());
        likeDislikeDto.setStatus(likeDisLike.getStatus());

        return likeDislikeDto;
    }

    LikeDislike dtoToLikeDislike(LikeDislikeDto likeDislikeDto, User user, Post post) {
        LikeDislike likeDisLike = new LikeDislike();
        likeDisLike.setId(likeDislikeDto.getId());
        likeDisLike.setPost(post);
        likeDisLike.setUser(user);
        likeDisLike.setStatus(likeDislikeDto.getStatus());

        return likeDisLike;
    }
}
