package com.beside.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String userId;
    private String nickname;
    private String callNo;
    private String token;
}
