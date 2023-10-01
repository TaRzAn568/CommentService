package com.example.Comment_Service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    Long id;
    String content;
    Long parentCommentId;
    List<CommentDto> childComments;
    Long postId;

}
