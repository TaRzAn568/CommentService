package com.example.Comment_Service.dto;

import com.example.Comment_Service.ENUM.LikeStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LikeDislikeDto {
    private Long id;
    private Long commentId;
    private Long userId;
    private Long postId;
    private LikeStatus status;

    // Constructors, getters, and setters

    // Constructors

}

