package com.beside.user.controller;

import com.beside.jwt.JwtProvider;
import com.beside.kakao.dto.KakaoUserInfoResponseDto;
import com.beside.kakao.service.KakaoService;
import com.beside.user.domain.UserEntity;
import com.beside.user.dto.*;
import com.beside.user.exception.UserException;
import com.beside.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "1.유저", description = "유저 관련 API")
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final KakaoService kakaoService;


    @PostMapping("/join")
    @Operation(summary = "회원 가입", description = "회원가입을 할 수 있습니다.")
    public ResponseEntity<?> join(@RequestBody SignUpRequest request) {
        SignUpResponse response = userService.joinUser(request);
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/kakaoJoin")
//    @Operation(summary = "카카오 회원 가입", description = "카카오 계정을 이용해 회원가입을 할 수 있습니다.")
//    public ResponseEntity<?> kakaoJoin(@RequestBody String accessToken) {
//        KakaoUserInfoResponseDto kakaoUser = kakaoService.getUserInfo(accessToken);
//        SignUpResponse response = userService.joinFromKakao(String.valueOf(kakaoUser.getId()), kakaoUser.getKakaoAccount().email);
//        return ResponseEntity.ok(response);
//    }


    @Operation(summary = "로그인", description = "로그인")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserInput userInput, HttpServletResponse servletResponse) {
        return ResponseEntity.ok(userService.login(userInput, servletResponse));
    }



    @Operation(summary = "카카오 로그인", description = "카카오 계정을 이용해 로그인을 할 수 있습니다. 계정이 없는 경우에는 회원가입 로직을 거친 후 토큰을 발급합니다." +
            "카카오 로그인 후 발급받은 인가코드를 body에 넣어서 요청합니다.")
    @GetMapping("/kakaoLogin")
    public ResponseEntity<?> kakaoLogin(@RequestParam String kakaoCode, HttpServletResponse servletResponse) {
        log.info("카카오 로그인 시작 : {}", kakaoCode);
//        KakaoUserInfoResponseDto kakaoUser = kakaoService.getUserInfo(code);
        return ResponseEntity.ok(userService.kakaoLogin(kakaoCode, servletResponse));
    }


    @Operation(summary = "내 정보 조회", description = "내 정보 조회")
    @GetMapping("/myPage")
    public ResponseEntity<?> myPage() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(userService.userInfo(userId));
    }

    @Operation(summary = "닉네임 변경")
    @PatchMapping("/updateNickname")
    public ResponseEntity<?> updateNickname(@RequestBody UpdateNicknameRequest request) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        String newNickname = request.getNickname();
        if (newNickname == null || newNickname.isEmpty()) {
            return ResponseEntity.badRequest().body("Nickname is missing or invalid.");
        }
        return ResponseEntity.ok(userService.updateNickname(userId, newNickname) + " : 변경 완료");
    }

    @Operation(summary = "비밀번호 변경")
    @PatchMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequest request) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.updatePassword(userId, request);
        return ResponseEntity.ok("변경 완료");
    }

    public String testBti(@RequestBody MbtiDto mbtiDto) {
        String result = userService.calResult(mbtiDto);
        return null;
    }



    /**
     * 테스트용
     * @param request
     * @param response
     * @param chain
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @GetMapping("/usernameTest")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "아이디 반환 성공"),
            @ApiResponse(responseCode = "500", description = "아이디 반환 실패")
    })
    @Operation(summary = "user id 반환 테스트", description = "테스트용 api")
    public String getUsername(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return "";
        }
        log.info("header : {}", header);
        String token = header.replace("Bearer ", "");
        return jwtProvider.parseToken(token).getSubject();
    }

}
