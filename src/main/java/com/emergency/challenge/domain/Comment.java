package com.emergency.challenge.domain;

import com.emergency.challenge.controller.request.CommentRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Builder
@Getter
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE posts SET deleted = true WHERE id = ?")
@NoArgsConstructor
@AllArgsConstructor
@Entity
//@Table(name="comment", indexes = @Index(name = "idx_title_deleted", columnList = "content, deleted",unique = true))
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long responseTo;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;


    @JoinColumn(name = "post_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean deleted;

    public void update(CommentRequestDto commentRequestDto) {
        this.content = commentRequestDto.getContent().replace("<script>","");
    }

    public boolean validateMember(Member member) {
        return !this.member.equals(member);
    }

    public void delete() {
        this.deleted = true;
    }
}