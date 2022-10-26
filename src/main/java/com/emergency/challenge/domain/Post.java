package com.emergency.challenge.domain;

import com.emergency.challenge.controller.request.PostRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@Where(clause = "deleted = false")
//@SQLDelete(sql = "UPDATE posts SET deleted = true WHERE id = ?")
@NoArgsConstructor
@AllArgsConstructor
@Entity
//@Table(name="post", indexes = @Index(name = "idx_title_deleted", columnList = "title, deleted",unique = true))
public class Post extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String content;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> comments;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Likes> likes;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Hates> hates;

  @JoinColumn(name = "member_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;

  @Column(nullable = false)
  private boolean deleted;

  public void update(PostRequestDto postRequestDto) {
    this.title = postRequestDto.getTitle();
    this.content = postRequestDto.getContent().replace("<",">");
  }

  public boolean validateMember(Member member) {
    return !this.member.equals(member);
  }

  public void delete() {
    this.deleted = true;
  }
}
