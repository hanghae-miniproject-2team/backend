package com.emergency.challenge.controller;

import com.emergency.challenge.controller.request.CommentRequestDto;
import com.emergency.challenge.controller.response.ResponseDto;
import com.emergency.challenge.domain.UserDetailsImpl;
import com.emergency.challenge.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Validated
@RequiredArgsConstructor
@RestController
@CustomBaseControllerAnnotation
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping(value = "/auth/comment")
    public ResponseDto<?> createComment(@RequestBody CommentRequestDto requestDto,
                                        HttpServletRequest request) {
        return commentService.createComment(requestDto, request);
    }


    // 작성한 댓글 조회 (마이페이지)
    @GetMapping(value = "/auth/comment")
    public ResponseDto<?> getMyComments(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.getMyCommentsByPost(userDetails);
    }


    // 전체 댓글 조회
    @GetMapping(value = "/comment/{id}")
    public ResponseDto<?> getAllComments(@PathVariable Long id) {
        return commentService.getAllCommentsByPost(id);
    }


    // 댓글 수정
    @PutMapping(value = "/auth/comment/{id}")
    public ResponseDto<?> updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto,
                                        HttpServletRequest request) {
        return commentService.updateComment(id, requestDto, request);
    }


    // 댓글 삭제
    @DeleteMapping(value = "/auth/comment/{id}")
    public ResponseDto<?> deleteComment(@PathVariable Long id,
                                        HttpServletRequest request) {
        return commentService.deleteComment(id, request);
    }
}