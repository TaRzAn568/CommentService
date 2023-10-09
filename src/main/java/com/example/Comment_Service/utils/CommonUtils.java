package com.example.Comment_Service.utils;

import com.example.Comment_Service.ENUM.LikeStatus;

public class CommonUtils {

    public static LikeStatus getReverseStatus(LikeStatus status) {
        if (status == LikeStatus.LIKE) {
            return LikeStatus.DISLIKE;
        } else {
            return LikeStatus.LIKE;
        }
    }
}
