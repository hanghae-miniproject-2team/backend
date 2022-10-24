package com.emergency.challenge.service;

import com.emergency.challenge.controller.response.ResponseDto;
import com.emergency.challenge.domain.Hates;
import com.emergency.challenge.domain.Member;
import com.emergency.challenge.domain.Post;
import com.emergency.challenge.domain.UserDetailsImpl;
import com.emergency.challenge.repository.HatesRepository;
import com.emergency.challenge.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HatesService {
    private final HatesRepository hatesRepository;
    private final PostService postService;
    private final PostRepository postRepository;

    public ResponseDto<?> hatesUp(Long postId, UserDetailsImpl userDetails) {
        System.out.println(postId);
        Member member = userDetails.getMember();
        Post post = postService.isPresentPost(postId);
        System.out.println("asdasds" + post);
        //라이크 디비에서 맴버아이디와 포스트아이디가 존재하는지 확인
        Optional<Hates> hates1 = hatesRepository.findByMemberAndPost(member, post);
        System.out.println(hates1);
        if (hates1.isPresent()) {
            hatesRepository.deleteById(hates1.get().getId());
            return ResponseDto.success(false);
        } else {
            Hates hates = Hates.builder()
                    .post(post)
                    .member(member)
                    .build();
            hatesRepository.save(hates);
            return ResponseDto.success(true);
        }
    }
}
