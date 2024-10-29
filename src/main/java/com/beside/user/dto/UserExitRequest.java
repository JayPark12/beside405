package com.beside.user.dto;

import lombok.Data;

@Data
public class UserExitRequest {
    private String refreshToken;
    private String reason;
}
