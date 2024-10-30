package com.beside.user.dto;

import lombok.Data;

@Data
public class UserExitRequest {
    private String userId;
    private String refreshToken;
    private String reason;
}
