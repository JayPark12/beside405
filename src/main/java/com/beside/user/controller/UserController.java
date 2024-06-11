package com.beside.user.controller;

import com.beside.jwt.JwtProvider;
import com.beside.user.domain.UserEntity;
import com.beside.user.dto.LoginResponse;
import com.beside.user.dto.SignUpRequest;
import com.beside.user.dto.SignUpResponse;
import com.beside.user.dto.UserInput;
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
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "유저", description = "유저 관련 API")
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;


    @PostMapping("/join")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 가입 성공"),
            @ApiResponse(responseCode = "500", description = "회원 가입 실패")
    })
    @Operation(summary = "회원 가입", description = "회원가입을 할 수 있습니다.")
    public ResponseEntity<?> join(@RequestBody SignUpRequest request) {
        SignUpResponse response = userService.joinUser(request);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "로그인", description = "로그인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "500", description = "로그인 실패")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserInput userInput, HttpServletResponse servletResponse) {
        try {
            return ResponseEntity.ok(userService.login(userInput, servletResponse));
        } catch (UserException e) {
            log.error("로그인 실패", e);
            return ResponseEntity.internalServerError().body("로그인 실패");
        }
    }


    @Operation(summary = "내 정보 조회", description = "내 정보 조회")
    public ResponseEntity<?> myPage() {
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
            chain.doFilter(request, response); // If not valid, go to the next filter.
            //return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 되지 않은 요청");
            return "";
        }
        log.info("header : {}", header);
        String token = header.replace("Bearer ", "");
        return jwtProvider.parseToken(token).getSubject();
    }

}
