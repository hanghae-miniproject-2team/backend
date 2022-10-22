package com.emergency.talk.service;

import com.emergency.talk.controller.request.LoginRequestDto;
import com.emergency.talk.controller.request.MemberRequestDto;
import com.emergency.talk.controller.response.MemberResponseDto;
import com.emergency.talk.controller.response.ResponseDto;
import com.emergency.talk.controller.response.TokenDto;
import com.emergency.talk.domain.Member;
import com.emergency.talk.error.ErrorCode;
import com.emergency.talk.jwt.TokenProvider;
import com.emergency.talk.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public ResponseDto<?> createMember(MemberRequestDto requestDto) {
        if (null != isPresentMember(requestDto.getNickname())) {
            return ResponseDto.fail(ErrorCode.ALREADY_SAVED_ID.name(),
                    ErrorCode.ALREADY_SAVED_ID.getMessage());
        }

        if (!requestDto.getPassword().equals(requestDto.getPasswordConfirm())) {
            return ResponseDto.fail(ErrorCode.PASSWORDS_NOT_MATCHED.name(),
                    ErrorCode.PASSWORDS_NOT_MATCHED.getMessage());
        }

        Member member = Member.builder()
                .nickname(requestDto.getNickname())
                .name(requestDto.getName())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .build();

        memberRepository.save(member);

        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .nickname(member.getNickname())
                        .name(member.getName())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .build()
        );
    }

    // 로그인
    @Transactional
    public ResponseDto<?> login(LoginRequestDto requestDto, HttpServletResponse response) {
        Member member = isPresentMember(requestDto.getNickname());
        if (null == member) {
            return ResponseDto.fail(ErrorCode.MEMBER_NOT_FOUND.name(),
                    ErrorCode.MEMBER_NOT_FOUND.getMessage());
        }

        if (!member.validatePassword(passwordEncoder, requestDto.getPassword())) {
            return ResponseDto.fail(ErrorCode.INVALID_MEMBER.name(), ErrorCode.INVALID_MEMBER.getMessage());
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        tokenToHeaders(tokenDto, response);

        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .nickname(member.getNickname())
                        .name(member.getName())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .build()
        );
    }


    // 로그아웃
    public ResponseDto<?> logout(HttpServletRequest request) {
//        Verification verification=new Verification();
//        verification.logout(request);
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail(ErrorCode.INVALID_TOKEN.name(), ErrorCode.INVALID_TOKEN.getMessage());
        }
        Member member = tokenProvider.getMemberFromAuthentication();
        if (null == member) {
            return ResponseDto.fail(ErrorCode.MEMBER_NOT_FOUND.name(),
                    ErrorCode.MEMBER_NOT_FOUND.getMessage());
        }
//        Member member = tokenProvider.getMemberFromAuthentication();
        return tokenProvider.deleteRefreshToken(member);
    }

    @Transactional(readOnly = true)
    public Member isPresentMember(String nickname) {
        Optional<Member> optionalMember = memberRepository.findByNickname(nickname);
        return optionalMember.orElse(null);
    }

    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }

    //토큰 유효하면 토큰값 쓰기
    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

//검증 과정 따로 빼기
//public class Verification{
//      public ResponseDto<Object> logout(HttpServletRequest request){
//          if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
//              return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
//          }
//          Member member = tokenProvider.getMemberFromAuthentication();
//          if (null == member) {
//              return ResponseDto.fail("MEMBER_NOT_FOUND",
//                      "사용자를 찾을 수 없습니다.");
//          }
//          return null;
//      }
//    }
}
