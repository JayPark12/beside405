package com.beside.user.dto;

import lombok.Data;

@Data
public class SignUpRequest {
    private String userId;
    private String password;
    private String nickname;
    private String callNo;
}
