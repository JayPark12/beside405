package com.beside.user.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class UserListResponse {
    private String userId;
    private String nickName;
    private String callNo;
}
