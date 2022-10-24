package com.emergency.challenge.controller;

import com.emergency.challenge.controller.response.ResponseDto;
import com.emergency.challenge.domain.UserDetailsImpl;
import com.emergency.challenge.service.HatesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CustomBaseControllerAnnotation
public class HateController {
    private final HatesService hatesService;

//    @ApiImplicitParams({
//            // 스웨거에서 할당해야하는 값들을 알려주는 Description
//            @ApiImplicitParam(
//                    name = "Refresh-Token",
//                    required = true,
//                    dataType = "string",
//                    paramType = "header"
//            )
//    })

    // 싫어요
    @PostMapping("/auth/post/{postid}/hate")
    public ResponseDto<?> Hates(@PathVariable Long postid, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return hatesService.hatesUp(postid, userDetails);
    }
}