package com.example.Comment_Service.services;

import com.example.Comment_Service.ENUM.LikeStatus;
import com.example.Comment_Service.dto.PostDto;
import com.example.Comment_Service.model.Comment;
import com.example.Comment_Service.model.Post;

import java.util.List;

public interface PostService { 
    
    PostDto createPost(PostDto PostDto);
    PostDto updatePost(PostDto postDto, Long id);
    PostDto gePostById(Long id);
    List<PostDto> getAllPostsByUser(Long userId);

    List<PostDto> getAllPost();
    void deletePost(Long id);

     void incrementLikesOrDislikes(Post post, LikeStatus likeStatus);

     void decrementLikesOrDislikes(Post post, LikeStatus likeStatus);
}