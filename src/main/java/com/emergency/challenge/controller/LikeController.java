package com.emergency.challenge.controller;

import com.emergency.challenge.controller.response.ResponseDto;
import com.emergency.challenge.domain.UserDetailsImpl;
import com.emergency.challenge.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CustomBaseControllerAnnotation
public class LikeController {
    private final LikesService likesService;

//    @ApiImplicitParams({
//            // 스웨거에서 할당해야하는 값들을 알려주는 Description
//            @ApiImplicitParam(
//                    name = "Refresh-Token",
//                    required = true,
//                    dataType = "string",
//                    paramType = "header"
//            )
//    })

    // 좋아요
    @PostMapping("/auth/post/{postid}/like")
    public ResponseDto<?> Likes(@PathVariable Long postid, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return likesService.likesUp(postid,userDetails);
    }

    // 좋아요 한 게시글 조회 (마이페이지)
//    @GetMapping("/auth/post/like")
//    public ResponseDto<?> LikesPost(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return likesService.LikesPost(userDetails);
//    }


}
