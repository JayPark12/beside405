package com.beside.Controller;

import com.beside.Entity.UserEntity;
import com.beside.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public String login(@RequestBody UserEntity user, HttpSession session) {
        UserEntity existingUser = userService.login(user.getId(), user.getPassword());
        if (existingUser != null) {
            session.setAttribute("id", user.getId());
            return "/main/mntiList";
        } else {
            return "/loginPg";
        }
    }
}
