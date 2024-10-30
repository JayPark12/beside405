package com.beside.user.controller;

import com.beside.jwt.JwtProvider;
import com.beside.kakao.dto.KakaoTokenResponseDto;
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
@CrossOrigin
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


    @Operation(summary = "로그인", description = "로그인")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserInput userInput, HttpServletResponse servletResponse) {
        return ResponseEntity.ok(userService.login(userInput, servletResponse));
    }


    @Operation(summary = "카카오 로그인", description = "카카오 계정을 이용해 로그인을 할 수 있습니다. 계정이 없는 경우에는 회원가입 로직을 거친 후 토큰을 발급합니다." +
            "카카오 로그인 후 발급받은 인가코드를 body에 넣어서 요청합니다.")
    @GetMapping("/kakaoLogin")
    public ResponseEntity<?> kakaoLogin(@RequestParam String code,
                                        @RequestParam(required = false) String scheduleId,
                                        HttpServletResponse servletResponse) {
        log.info("카카오 로그인 시작 : {}", code);

        //1. 인가코드로 토큰 발급
        KakaoTokenResponseDto getToken = kakaoService.getAccessTokenFromKakao(code);

        String accessToken = getToken.getAccessToken();
        String refreshToken = getToken.getRefreshToken();

        //2. 토큰으로 카카오 회원정보
        KakaoUserInfoResponseDto kakaoUser = kakaoService.getUserInfo(accessToken);

        //3. 로그인 처리
        return ResponseEntity.ok(userService.kakaoLogin2(kakaoUser, scheduleId, servletResponse, refreshToken));
    }


    @Operation(summary = "카카오 회원 탈퇴", description = "카카오 연결끊기 + db에서 회원 삭제처리")
    @PostMapping("/kakaoDelete")
    public ResponseEntity<?> deleteUser(@RequestBody UserExitRequest request) {
        //1. refresh token으로 카카오 액세스 토큰 갱신
        String accessToken = kakaoService.renewToken(request.getRefreshToken());

        //2. access token으로 연결 끊기
        String id = kakaoService.unlink(accessToken);

        //3. db에서 정보 삭제
        String result = userService.deleteUser(request.getUserId(), request.getReason());

        return ResponseEntity.ok("회원 삭제 완료. user id :  " + result);
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
