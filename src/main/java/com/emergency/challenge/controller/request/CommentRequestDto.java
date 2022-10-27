package com.emergency.challenge.controller.request;

import com.emergency.challenge.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {
    private Long postId;
    private String content;
    private Long responseTo=null;
//    private boolean deleted = false;
//   public CommentRequestDto(CommentRequestDto commentRequestDto){
//        this.content=commentRequestDto.getContent().replace("<script>","");
//    }
}