package com.example.Comment_Service.services;

import com.example.Comment_Service.ENUM.LikeStatus;
import com.example.Comment_Service.dto.PostDto;
import com.example.Comment_Service.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService { 
    
    PostDto createPost(PostDto PostDto);
    PostDto updatePost(PostDto postDto, Long id);
    PostDto gePostById(Long id);
    Page<PostDto> getAllPostsByUser(Long userId, Pageable pageable);

    Page<PostDto> getAllPost(Pageable pageable);
    void deletePost(Long id);

     void incrementLikesOrDislikes(Post post, LikeStatus likeStatus);

     void decrementLikesOrDislikes(Post post, LikeStatus likeStatus);
}