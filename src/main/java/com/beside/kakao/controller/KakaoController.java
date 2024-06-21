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

        //https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=5f6f8c9bfc5b55950abf9076fb71813e&redirect_uri=http://localhost:3010/callback

        return "login";
    }

    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code) {
        String accessToken = kakaoService.getAccessTokenFromKakao(code);
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
