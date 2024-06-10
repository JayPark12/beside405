package com.beside.user.controller;

import com.beside.user.domain.UserEntity;
import com.beside.user.dto.LoginResponse;
import com.beside.user.dto.SignUpRequest;
import com.beside.user.dto.SignUpResponse;
import com.beside.user.dto.UserInput;
import com.beside.user.exception.UserException;
import com.beside.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Operation(summary = "로그인", description = "로그인을 합니다.", tags = { "User Controller" })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserInput userInput, HttpServletResponse servletRequest) {
        try {
            return ResponseEntity.ok(userService.login(userInput, servletRequest));
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

    @Operation(summary = "유저 리스트", description = "전체 유저 리스트를 가져옵니다.", tags = { "User Controller" })
    @GetMapping("/list")
    public ResponseEntity<?> userList(HttpServletRequest servletRequest) {
        String userId = (String) servletRequest.getAttribute("userId");
        return ResponseEntity.ok(userService.getUserList(userId));
    }


}
