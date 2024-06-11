package com.beside.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "로그인 완료 응답 DTO")
public class LoginResponse {
    @Schema(description = "유저 id")
    private String userId;

    @Schema(description = "닉네임")
    private String nickname;

    @Schema(description = "전화번호")
    private String callNo;

    @Schema(description = "jwt token")
    private String token;

    @Schema(description = "bearer token")
    private String bearerToken;

}
