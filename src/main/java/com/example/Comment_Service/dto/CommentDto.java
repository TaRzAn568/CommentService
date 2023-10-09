package com.example.Comment_Service.dto;

import com.example.Comment_Service.model.Comment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    Long id;
    String text;
    int likes;
    int dislikes;
    Long post_Id;
    Long user_Id;

}
