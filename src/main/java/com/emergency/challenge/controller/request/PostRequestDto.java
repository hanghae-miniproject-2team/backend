package com.emergency.challenge.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRequestDto {
  private String title;
  private String content;
//  private boolean deleted = false;

  public PostRequestDto(String title, String content) {
    this.title = title.replace("<script>","");
    this.content = content.replace("<script>","");
  }
}
