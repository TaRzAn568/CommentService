package com.example.Comment_Service.mapping;

import com.example.Comment_Service.dto.CommentDto;
import com.example.Comment_Service.dto.LikeDislikeDto;
import com.example.Comment_Service.dto.UserDto;
import com.example.Comment_Service.model.Comment;
import com.example.Comment_Service.model.LikeDislike;
import com.example.Comment_Service.model.Post;
import com.example.Comment_Service.model.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LikeDislikeMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public LikeDislikeMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    public LikeDislikeDto toDto(LikeDislike likeDisLike) {
        PropertyMap<LikeDislike, LikeDislikeDto> propertyMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setPostId(source.getPost().getId());
                map().setUserId(source.getUser().getId());
            }
        };
        TypeMap<LikeDislike, LikeDislikeDto> typeMap = modelMapper.getTypeMap(LikeDislike.class, LikeDislikeDto.class);
        if (typeMap == null) {
            modelMapper.addMappings(propertyMap);
        }
        return modelMapper.map(likeDisLike, LikeDislikeDto.class);
    }

    public LikeDislike toEntity(LikeDislikeDto likeDislikeDto) {

        PropertyMap<LikeDislikeDto, LikeDislike> propertyMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                map().getUser().setId(source.getUserId());
                map().getPost().setId(source.getPostId());
            }
        };
        TypeMap<LikeDislikeDto, LikeDislike> typeMap = modelMapper.getTypeMap(LikeDislikeDto.class, LikeDislike.class);
        if (typeMap == null) {
            modelMapper.addMappings(propertyMap);
        }
        return modelMapper.map(likeDislikeDto, LikeDislike.class);
    }
}
