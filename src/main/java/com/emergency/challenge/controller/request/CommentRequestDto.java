package com.emergency.challenge.controller.request;

import com.emergency.challenge.domain.Member;
import lombok.Getter;

@Getter
public class CommentRequestDto {
    private Long postId;
    private String comment;
    private Member member;
}