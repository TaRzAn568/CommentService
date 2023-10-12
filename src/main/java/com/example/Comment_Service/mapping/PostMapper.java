package com.example.Comment_Service.mapping;

import com.example.Comment_Service.dto.PostDto;
import com.example.Comment_Service.entity.Post;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public PostMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PostDto toDto(Post post) {
        PropertyMap<Post, PostDto> propertyMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setUser_id(source.getUser().getId());
            }
        };

        TypeMap<Post, PostDto> typeMap = modelMapper.getTypeMap(Post.class, PostDto.class);
        if (typeMap == null) {
            modelMapper.addMappings(propertyMap);
        }
        return modelMapper.map(post, PostDto.class);
    }

    public Post toEntity(PostDto postDto) {
        PropertyMap<PostDto, Post> propertyMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                map().getUser().setId(source.getUser_id());
            }
        };
        TypeMap<PostDto, Post> typeMap = modelMapper.getTypeMap(PostDto.class, Post.class);
        if (typeMap == null) {
            modelMapper.addMappings(propertyMap);
        }
        return modelMapper.map(postDto, Post.class);
    }
}
