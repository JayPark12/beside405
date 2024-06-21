package com.beside.kakao.controller;

import com.beside.kakao.dto.KakaoUserInfoResponseDto;
import com.beside.kakao.service.KakaoService;
import com.beside.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/kakao")
public class KakaoController {
    private final UserService userService;
    private final KakaoService kakaoService;

    @Value("${kakao.client_id}")
    private String client_id;

    @Value("${kakao.redirect_uri}")
    private String redirect_uri;

    @Value("${kakao.callback_url}")
    private String kakaoCallbackUrl;

    @GetMapping("/page")
    public String loginPage(@RequestParam String clientId, @RequestParam String redirectUri) {
        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+client_id+"&redirect_uri="+redirect_uri;
        //String returnUrl = niceCallbackUrl +
        //https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}
        //https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=5f6f8c9bfc5b55950abf9076fb71813e&redirect_uri=http://localhost:3010/kakao/callback
        return "login";
    }

    @GetMapping("/getToken")
    public String getToken(@RequestParam("code") String code) {
        return kakaoService.getAccessTokenFromKakao(code);
    }

    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code) {
        // 1. 인가 코드 받기 (RequestParam String code)

        // 2. 토큰 받기
        String accessToken = kakaoService.getAccessTokenFromKakao(code);

        // 3. 사용자 정보 받기
        //첫번째 방법
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);
        //두번째 방법
        Map<String, Object> userInfo2 = kakaoService.getUserInfo2(accessToken);

        return ResponseEntity.ok(userInfo);
    }


    /* 헤더에 토큰 필수
       //정보조회 : https://kapi.kakao.com/v2/user/me
       //로그아웃 : https://kapi.kakao.com/v1/user/logout
       //연결끊기 : https://kapi.kakao.com/v1/user/unlink
     */




}
