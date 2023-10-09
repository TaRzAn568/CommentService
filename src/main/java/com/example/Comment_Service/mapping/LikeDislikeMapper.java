package com.example.Comment_Service.mapping;

import com.example.Comment_Service.dto.LikeDislikeDto;
import com.example.Comment_Service.dto.UserDto;
import com.example.Comment_Service.model.Comment;
import com.example.Comment_Service.model.LikeDislike;
import com.example.Comment_Service.model.Post;
import com.example.Comment_Service.model.User;
import org.modelmapper.ModelMapper;
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

        LikeDislikeDto likeDislikeDto = new LikeDislikeDto();
        likeDislikeDto.setId(likeDisLike.getId());
        likeDislikeDto.setCommentId(likeDisLike.getComment().getId());
        likeDislikeDto.setUserId(likeDisLike.getUser().getId());
        likeDislikeDto.setStatus(likeDisLike.getStatus());

        return likeDislikeDto;
    }

    public LikeDislike toEntity(LikeDislikeDto likeDislikeDto, User user, Comment comment, Post post) {
        LikeDislike likeDisLike = new LikeDislike();
        likeDisLike.setId(likeDislikeDto.getId());
        likeDisLike.setComment(comment);
        likeDisLike.setUser(user);
        likeDisLike.setStatus(likeDislikeDto.getStatus());

        return likeDisLike;
    }
}
