package com.example.Comment_Service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
