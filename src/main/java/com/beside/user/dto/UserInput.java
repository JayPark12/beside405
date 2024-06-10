package com.beside.user.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(description = "로그인 요청 DTO")
public class UserInput {

    @Schema(description = "id")
    private String id;

    @Schema(description = "비밀번호")
    private String password;

}
