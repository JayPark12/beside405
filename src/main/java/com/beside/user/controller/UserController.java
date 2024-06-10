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
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    @Operation(summary = "로그인", description = "로그인을 합니다.", tags = { "User Controller" })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserInput userInput, HttpServletResponse servletResponse) {
        try {
            return ResponseEntity.ok(userService.login(userInput, servletResponse));
        } catch (UserException e) {
            log.error("로그인 실패", e);
            return ResponseEntity.internalServerError().body("로그인 실패");
        }
    }

    @Operation(summary = "회원가입", description = "회원가입을 합니다.", tags = { "User Controller" })
    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody SignUpRequest request) {
        SignUpResponse response = userService.joinUser(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "유저 리스트", description = "전체 유저 리스트를 가져옵니다. 어드민 계정만 조회 가능합니다.", tags = { "User Controller" })
    @GetMapping("/list")
    public ResponseEntity<?> userList(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");

        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is missing or invalid.");
        }

        return ResponseEntity.ok(userService.getUserList(userId));
    }

    @GetMapping("/usernameTest")
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
