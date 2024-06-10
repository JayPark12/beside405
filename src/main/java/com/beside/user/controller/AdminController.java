package com.beside.user.controller;

import com.beside.user.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "관리자", description = "관리자 관련 API")
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @Operation(summary = "회원 정보 리스트", description = "회원 정보 리스트를 가져옵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 리스트 반환 성공"),
            @ApiResponse(responseCode = "500", description = "유저 리스트 반환 실패")
    })
    @GetMapping("/userList")
    public ResponseEntity<?> userList(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");

        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is missing or invalid.");
        }

        return ResponseEntity.ok(adminService.getUserList(userId));
    }
}
