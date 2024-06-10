package com.beside.user.controller;

import com.beside.user.domain.UserEntity;
import com.beside.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public String login(@RequestBody UserEntity userEntity) {

        return userService.login(userEntity);
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody UserEntity userEntity) {
        UserEntity savedUser = userService.joinUser(userEntity);
        return ResponseEntity.ok(savedUser);
    }



}