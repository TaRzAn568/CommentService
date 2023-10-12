package com.example.Comment_Service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    private Long id;
    private String title;
    private String content;
    int likes;
    int dislikes;
    private Long user_id;
    private Date createDate;

}
