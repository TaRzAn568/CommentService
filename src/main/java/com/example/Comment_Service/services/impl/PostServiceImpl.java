package com.example.Comment_Service.services.impl;

import com.example.Comment_Service.dto.PostDto;

import com.example.Comment_Service.exception.ResourceNotFoundException;
import com.example.Comment_Service.model.Post;
import com.example.Comment_Service.model.User;
import com.example.Comment_Service.repository.PostRepository;
import com.example.Comment_Service.repository.UserRepository;
import com.example.Comment_Service.services.PostService;
import com.example.Comment_Service.utils.ModelMapperConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    
    @Autowired
    ModelMapperConfig modelMapperConfig;
    
    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;
    @Override
    public PostDto createPost(PostDto postDto) {
        Post post = dtoToPost(postDto);
        Post savedPost = postRepository.save(post);
        return postToDto(savedPost);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Long id) {
        Post post = postRepository.findById(id).orElseThrow(()->(new ResourceNotFoundException("Post", "id", id)));
        post.setContent(postDto.getContent());
        post.setTitle(postDto.getTitle());
       

        postRepository.save(post);
        return postDto;
    }

    @Override
    public PostDto gePostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()->(new ResourceNotFoundException("Post", "id", id)));
        return postToDto(post);     }

    @Override
    public List<PostDto> getAllPostsByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "userId",userId));
        List<Post> posts = postRepository.findByUser(user);
        return posts.stream().map(this::postToDto).collect(Collectors.toList());    }

    @Override
    public void deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()->(new ResourceNotFoundException("Post", "id", id)));
        postRepository.delete(post);
    }

    private Post dtoToPost(PostDto postDto){
        return modelMapperConfig.modelMapper().map(postDto, Post.class);
    }
    private PostDto postToDto(Post post){
        return modelMapperConfig.modelMapper().map(post, PostDto.class);
    }
}
