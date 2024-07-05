package com.beside.kakao.controller;

import com.beside.kakao.dto.KakaoUserInfoResponseDto;
import com.beside.kakao.service.KakaoService;
import com.beside.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin
//@Tag(name = "1-1.카카오 유저", description = "카카오 로그인 관련 API")
@RequestMapping("/kakao")
public class KakaoController {
    private final UserService userService;
    private final KakaoService kakaoService;
    private final RestTemplate restTemplate;

    @Value("${kakao.client_id}")
    private String clientId;

    @Value("${kakao.redirect_uri}")
    private String redirectUrI;

    @Value("${kakao.callback_url}")
    private String kakaoCallbackUrl;

//    @GetMapping("/login")
//    @Operation(summary = "카카오 로그인", description = "카카오 계정을 이용해 로그인 또는 회원가입을 할 수 있습니다. 로그인이 완료되면 parameter로 code가 발급됩니다.")
    public void kakaoLogin(HttpServletResponse response) throws IOException {
        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + clientId + "&redirect_uri=" + redirectUrI;
        //https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}
        //https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=5f6f8c9bfc5b55950abf9076fb71813e&redirect_uri=http://localhost:3010/kakao/callback
        //https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=5f6f8c9bfc5b55950abf9076fb71813e&redirect_uri=http://localhost:3010/kakao/getToken
        //https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=5f6f8c9bfc5b55950abf9076fb71813e&redirect_uri=http://223.130.162.119:3010/kakao/callback
        //https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=5f6f8c9bfc5b55950abf9076fb71813e&redirect_uri=http://223.130.162.119:3010/kakao/getToken
        response.sendRedirect(location);
    }

//    @GetMapping("/getToken")
//    @Operation(summary = "토큰 발급", description = "로그인 후 발급받은 code를 이용해 토큰을 발급합니다.")
    public String getToken(@RequestParam("code") String code) {
        return kakaoService.getAccessTokenFromKakao(code);
    }

//    @GetMapping("/callback")
//    @Operation(summary = "토큰 발급", description = "로그인 후 발급받은 code를 이용해 내 정보를 조회합니다.")
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


//    @Operation(summary = "내 정보조회", description = "로그인 후 발급 받은 토큰을 헤더에 넣어 내 정보를 조회합니다.")
//    @GetMapping("/myInfo")
    public Mono<String> kakaoMyInfo(@RequestHeader("Authorization") String accessToken) {
        return kakaoService.kakaoMyInfo(accessToken);
    }

//    @Operation(summary = "카카오 로그아웃", description = "로그인 후 발급 받은 토큰을 헤더에 넣어 현재 연결되어 있는 계정을 로그아웃 합니다.")
//    @GetMapping("/logout")
    public String kakaoLogout(@RequestHeader("Authorization") String accessToken) {
        String url = "https://kapi.kakao.com/v1/user/logout";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return response.getBody();
    }

//    @Operation(summary = "카카오 회원 탈퇴", description = "로그인 후 발급 받은 토큰을 헤더에 넣어 현재 연결되어 있는 계정의 연결을 끊습니다.")
//    @GetMapping("/deleteUser")
    public String kakaoDeleteUser(@RequestHeader("Authorization") String accessToken) {
        String url = "https://kapi.kakao.com/v1/user/unlink";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return response.getBody();
    }


    /* 헤더에 토큰 필수
       //정보조회 : https://kapi.kakao.com/v2/user/me
       //로그아웃 : https://kapi.kakao.com/v1/user/logout
       //연결끊기 : https://kapi.kakao.com/v1/user/unlink
     */




}
