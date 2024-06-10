package com.beside.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "회원 가입 반환 DTO")
public class SignUpResponse {
    @Schema(description = "가입 완료 id")
    private String userId;

    @Schema(description = "설명")
    private String desc;
}
