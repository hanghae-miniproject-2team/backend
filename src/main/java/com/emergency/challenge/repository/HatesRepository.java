package com.emergency.challenge.repository;

import com.emergency.challenge.domain.Hates;
import com.emergency.challenge.domain.Member;
import com.emergency.challenge.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HatesRepository extends JpaRepository<Hates, Long> {

    Optional<Hates> findByMemberAndPost(Member member, Post post);
    List<Hates> findByPost(Post post);


    List<Hates> findAllByMember(Member member);
}