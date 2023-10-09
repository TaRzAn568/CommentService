package com.example.Comment_Service.services.impl;

import com.example.Comment_Service.ENUM.LikeStatus;
import com.example.Comment_Service.dto.PostDto;

import com.example.Comment_Service.exception.ResourceNotFoundException;
import com.example.Comment_Service.mapping.PostMapper;
import com.example.Comment_Service.model.Post;
import com.example.Comment_Service.model.User;
import com.example.Comment_Service.repository.PostRepository;
import com.example.Comment_Service.repository.UserRepository;
import com.example.Comment_Service.services.PostService;
import com.example.Comment_Service.utils.ModelMapperConfig;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
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

    @Autowired
    PostMapper postMapper;
    @Override
    public PostDto createPost(PostDto postDto) {


        User user = userRepository.findById(postDto.getUser_id()).orElseThrow(()-> new ResourceNotFoundException("User", "userId",postDto.getUser_id()));
        Post post =  postMapper.toEntity(postDto);
        post.setCreateDate(new Date());
        Post savedPost = postRepository.save(post);
        return postMapper.toDto(savedPost);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Long id) {
        Post post = postRepository.findById(id).orElseThrow(()->(new ResourceNotFoundException("Post", "id", id)));
        if(postDto.getContent() != null && !postDto.getContent().isEmpty())
            post.setContent(postDto.getContent());
        if(postDto.getTitle() != null &&  !postDto.getTitle().isEmpty())
            post.setTitle(postDto.getTitle());
        return postMapper.toDto(postRepository.save(post));
    }

    @Override
    public PostDto gePostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()->(new ResourceNotFoundException("Post", "id", id)));
        return postMapper.toDto(post);     }

    @Override
    public List<PostDto> getAllPostsByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "userId",userId));
        List<Post> posts = postRepository.findByUser(user);
        return posts.stream().map(post -> postMapper.toDto(post)).collect(Collectors.toList());    }

    @Override
    public List<PostDto> getAllPost() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(post -> postMapper.toDto(post)).collect(Collectors.toList());
    }

    @Override
    public void deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()->(new ResourceNotFoundException("Post", "id", id)));
        postRepository.delete(post);
    }

    public void incrementLikesOrDislikes(Post post, LikeStatus likeStatus) {
        if (likeStatus == LikeStatus.LIKE) {
            post.setLikes(post.getLikes() + 1);
        } else if (likeStatus == LikeStatus.DISLIKE) {
            post.setDislikes(post.getDislikes() + 1);
        }
        postRepository.save(post);
    }

    public void decrementLikesOrDislikes(Post post, LikeStatus likeStatus) {
        if (likeStatus == LikeStatus.LIKE && post.getLikes() > 0) {
            post.setLikes(post.getLikes() - 1);
        } else if (likeStatus == LikeStatus.DISLIKE && post.getDislikes() > 0) {
            post.setDislikes(post.getDislikes() - 1);
        }
        postRepository.save(post);
    }
}
