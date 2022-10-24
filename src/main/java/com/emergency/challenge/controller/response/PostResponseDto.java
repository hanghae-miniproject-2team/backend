package com.emergency.challenge.controller.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
  private Long id;
  private String title;
  private String content;
  private String author;
  private Long likesCount;
  private Long hatesCount;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
}
