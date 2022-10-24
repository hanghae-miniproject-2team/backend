package com.emergency.challenge.repository;

import com.emergency.challenge.domain.Likes;
import com.emergency.challenge.domain.Member;
import com.emergency.challenge.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    Optional<Likes> findByMemberAndPost(Member member, Post post);
    List<Likes>findByPost(Post post);


    List<Likes> findAllByMember(Member member);
}
