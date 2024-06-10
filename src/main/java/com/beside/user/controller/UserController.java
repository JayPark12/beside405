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

    @Operation(summary = "로그인", description = "로그인")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserInput userInput, HttpServletResponse servletResponse) {
        try {
            return ResponseEntity.ok(userService.login(userInput, servletResponse));
        } catch (UserException e) {
            log.error("로그인 실패", e);
            return ResponseEntity.internalServerError().body("로그인 실패");
        }
    }

    @PostMapping("/join")
    @Operation(summary = "회원 가입", description = "회원가입을 할 수 있습니다.")
    public ResponseEntity<?> join(@RequestBody SignUpRequest request) {
        SignUpResponse response = userService.joinUser(request);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/usernameTest")
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
