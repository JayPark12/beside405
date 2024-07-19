package com.beside.user.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder
public class UserInfoResponse {
    private String userId;
    private String nickname;
    private String callNo;
    private LocalDateTime creatDt;
}
