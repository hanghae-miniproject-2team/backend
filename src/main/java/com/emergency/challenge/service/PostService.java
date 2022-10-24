package com.emergency.challenge.service;

import com.emergency.challenge.controller.request.PostRequestDto;
import com.emergency.challenge.controller.response.PostResponseDto;
import com.emergency.challenge.controller.response.ResponseDto;
import com.emergency.challenge.domain.*;
import com.emergency.challenge.jwt.TokenProvider;
import com.emergency.challenge.repository.HatesRepository;
import com.emergency.challenge.repository.LikesRepository;
import com.emergency.challenge.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final LikesRepository likesRepository;
  private final TokenProvider tokenProvider;

  private final HatesRepository hatesRepository;

  // 게시글 작성
  @Transactional
  public ResponseDto<?> createPost(PostRequestDto requestDto, HttpServletRequest request) {
    if (null == request.getHeader("Refresh-Token")) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
          "로그인이 필요합니다.");
    }

    if (null == request.getHeader("Authorization")) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
          "로그인이 필요합니다.");
    }

    Member member = validateMember(request);
    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }

    Post post = Post.builder()
        .title(requestDto.getTitle())
        .content(requestDto.getContent())
        .member(member)
        .build();

    postRepository.save(post);

    return ResponseDto.success(
        PostResponseDto.builder()
            .id(post.getId())
            .title(post.getTitle())
            .content(post.getContent())
            .author(post.getMember().getNickname())
                .likesCount(0L)
                .hatesCount(0L)
                .createdAt(post.getCreatedAt())
            .modifiedAt(post.getModifiedAt())
            .build()
    );
  }

  // 게시글 조회
  @Transactional(readOnly = true)
  public ResponseDto<?> getPost(Long id) {
    Post post = isPresentPost(id);
    if (null == post) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
    }

    //게시글 졸아요 리스트
    List<Likes> likesCount=likesRepository.findByPost(post);
    List<Hates> hatesCount = hatesRepository.findByPost(post);
    return ResponseDto.success(
            PostResponseDto.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .likesCount((long) likesCount.size())
                    .hatesCount((long) hatesCount.size())
                    .author(post.getMember().getNickname())
                    .createdAt(post.getCreatedAt())
                    .modifiedAt(post.getModifiedAt())
                    .build()
    );
  }


  // 작성한 게시글 조회 (마이페이지)
  @Transactional(readOnly = true)
  public ResponseDto<?> getMyPost(UserDetailsImpl userDetails) {
    // 현재 저장된 유저의 id로 작성된 post들 불러옴
    List<Post> posts = postRepository.findAllByMember_Id(userDetails.getMember().getId());
    // 작성 post가 없으면 실패 처리
    if (posts.isEmpty()) {
      return ResponseDto.fail("NOT_FOUND", "해당 유저가 작성한 게시글이 존재하지 않습니다.");
    }

    // 최종적으로 작성한 post들의 정보를 저장하여 출력하기 위한 postlist 생성
    List<PostResponseDto> postlist = new ArrayList<>();

    // 작성 post들의 각 정보를 PostResponseDto에 기록하여 postlist에 저장
    for(Post post : posts){
      List<Likes> likesCount=likesRepository.findByPost(post);
      List<Hates> hatesCount = hatesRepository.findByPost(post);
      postlist.add(
              PostResponseDto.builder()
                      .id(post.getId())
                      .title(post.getTitle())
                      .author(post.getMember().getNickname())
//                      .category(post.getCategory())
                      .likesCount((long) likesCount.size())
                      .hatesCount((long) hatesCount.size())
                      .content(post.getContent())
                      .createdAt(post.getCreatedAt())
                      .modifiedAt(post.getModifiedAt())
                      .build()
      );
    }

    // 최종적으로 저장된 작성 post들을 출력
    return ResponseDto.success(postlist);
  }


  // 게시글 전체 조회
//  @Transactional(readOnly = true)
//  public ResponseDto<?> getAllPost(int page, int size, String sortBy, boolean isAsc) {
//
//    Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
//    Sort sort = Sort.by(direction, sortBy);
//    Pageable pageable = PageRequest.of(page, size, sort);
//    Page<Post> post=postRepository.findAll(pageable);
//    return ResponseDto.success(postRepository.findAll(pageable));
////    return ResponseDto.success(postRepository.findAllByOrderByModifiedAtDesc());
//  }
  @Transactional(readOnly = true)
  public ResponseDto<?> getAllPost(Pageable pageable) {
    return ResponseDto.success(postRepository.findAll(pageable));
//    return ResponseDto.success(postRepository.findAllByOrderByModifiedAtDesc());
  }


  // 게시글 수정
  @Transactional
  public ResponseDto<Post> updatePost(Long id, PostRequestDto requestDto, HttpServletRequest request) {
    if (null == request.getHeader("Refresh-Token")) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
          "로그인이 필요합니다.");
    }

    if (null == request.getHeader("Authorization")) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
          "로그인이 필요합니다.");
    }

    Member member = validateMember(request);
    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }

    Post post = isPresentPost(id);
    if (null == post) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
    }

    if (post.validateMember(member)) {
      return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
    }

    post.update(requestDto);
    return ResponseDto.success(post);
  }


  // 게시글 삭제
  @Transactional
  public ResponseDto<?> deletePost(Long id, HttpServletRequest request) {
    if (null == request.getHeader("Refresh-Token")) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
          "로그인이 필요합니다.");
    }

    if (null == request.getHeader("Authorization")) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
          "로그인이 필요합니다.");
    }

    Member member = validateMember(request);
    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }

    Post post = isPresentPost(id);
    if (null == post) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
    }

    if (post.validateMember(member)) {
      return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
    }

    postRepository.delete(post);
    return ResponseDto.success("delete success");
  }

  // 마이페이지 게시글 리스트
  @Transactional(readOnly = true)
  public List<Post> existMyPost(Long id) {
    // 게시글이 여러개를 작성했을 수도 있기에 List로 받음
    List<Post> Postlist = postRepository.findAllByMember_Id(id);
    return Postlist;
  }

  @Transactional(readOnly = true)
  public Post isPresentPost(Long id) {
    Optional<Post> optionalPost = postRepository.findById(id);
    return optionalPost.orElse(null);
  }

  @Transactional
  public Member validateMember(HttpServletRequest request) {
    if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
      return null;
    }
    return tokenProvider.getMemberFromAuthentication();
  }

}
