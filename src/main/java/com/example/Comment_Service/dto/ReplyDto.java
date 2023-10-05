package com.example.Comment_Service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDto {

    private String id;
    private String text;
    private int likes;
    private int dislikes;
    private Long user_Id;
    private Long parent_comment_Id;



}