package com.beside.user.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data @Builder
public class UserInfoResponse {
    private String userId;
    private String nickname;
    private String callNo;
    private LocalDate creatDt;
}
