package com.example.Comment_Service.mapping;

import com.example.Comment_Service.dto.CommentDto;
import com.example.Comment_Service.entity.Comment;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public CommentMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CommentDto toDto(Comment comment) {

        PropertyMap<Comment, CommentDto> propertyMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setPost_Id(source.getPost().getId());
                map().setUser_Id(source.getUser().getId());
            }
        };
        TypeMap<Comment, CommentDto> typeMap = modelMapper.getTypeMap(Comment.class, CommentDto.class);
        if (typeMap == null) {
            modelMapper.addMappings(propertyMap);
        }
        return modelMapper.map(comment, CommentDto.class);
    }

    public Comment toEntity(CommentDto commentDto) {
        PropertyMap<CommentDto, Comment> propertyMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                map().getUser().setId(source.getUser_Id());
                map().getPost().setId(source.getPost_Id());
            }
        };
        TypeMap<CommentDto, Comment> typeMap = modelMapper.getTypeMap(CommentDto.class, Comment.class);
        if (typeMap == null) {
            modelMapper.addMappings(propertyMap);
        }
        return modelMapper.map(commentDto, Comment.class);
    }
}
