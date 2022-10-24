package com.emergency.challenge.controller;

import com.emergency.challenge.controller.request.CommentRequestDto;
import com.emergency.challenge.controller.response.ResponseDto;
import com.emergency.challenge.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@CustomBaseControllerAnnotation
public class CommentController {
    private final CommentService commentService;

    //댓글 쓰기
    @PostMapping( "/api/auth/comment")
    public ResponseDto<?> createComment(@RequestBody CommentRequestDto requestDto,
                                        HttpServletRequest request) {
        return commentService.createComment(requestDto, request);
    }

    @GetMapping("/api/comment/{id}")
    public ResponseDto<?> getAllComments(@PathVariable Long id) {

        return commentService.getAllCommentsByPost(id);
    }

    @PutMapping( "/api/auth/comment/{id}")
    public ResponseDto<?> updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto,
                                        HttpServletRequest request) {
        return commentService.updateComment(id, requestDto, request);
    }

    @DeleteMapping( "/api/auth/comment/{id}")
    public ResponseDto<?> deleteComment(@PathVariable Long id,
                                        HttpServletRequest request) {
        return commentService.deleteComment(id, request);
    }
}