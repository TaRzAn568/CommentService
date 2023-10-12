package com.example.Comment_Service.controller;

import com.example.Comment_Service.dto.ApiResponse;
import com.example.Comment_Service.dto.PostDto;
import com.example.Comment_Service.services.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping(path = "api/v1/posts")
public class PostController {


    @Autowired
    PostService postService;
    @PostMapping("/create")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto) {
        PostDto createPostDto = postService.createPost(postDto);
        return new ResponseEntity<>(createPostDto, HttpStatus.CREATED);

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable("id") Long id) {
        PostDto updatedPost = postService.updatePost(postDto, id);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Object>> deletePost(@PathVariable("id") Long id) {
        postService.deletePost(id);
        return new ResponseEntity<>(new ApiResponse<>("Post deleted successfully", true, null), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable("id") Long id) {
        PostDto postDto = postService.gePostById(id);
        return new ResponseEntity<>(postDto,HttpStatus.OK);

    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<PostDto>> getAllPostByUser(@PathVariable Long userId,
                                                          @RequestParam(value = "page", defaultValue = "0") int page,
                                                          @RequestParam(value = "size", defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostDto> postDtos = postService.getAllPostsByUser(userId, pageable);
        return new ResponseEntity<>(postDtos,HttpStatus.OK);

    }

    @GetMapping("/")
    public ResponseEntity<Page<PostDto>> getAllPost(@RequestParam(value = "page", defaultValue = "0") int page,
                                                    @RequestParam(value = "size", defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostDto> postDtos = postService.getAllPost(pageable);
        return new ResponseEntity<>(postDtos,HttpStatus.OK);

    }
}
