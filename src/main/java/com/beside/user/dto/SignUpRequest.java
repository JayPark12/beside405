package com.beside.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "회원 가입 요청 DTO")
public class SignUpRequest {
    @Schema(description = "유저 id")
    private String userId;

    @Schema(description = "비밀번호")
    private String password;

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "휴대폰 번호")
    private String callNo;
}
