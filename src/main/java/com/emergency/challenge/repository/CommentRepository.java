package com.emergency.challenge.repository;

import com.emergency.challenge.domain.Comment;
import com.emergency.challenge.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);
    List<Comment> findAllByMember_id(Long Id);
    void deleteByIdOrResponseTo(Long id, Long responseTo);

}