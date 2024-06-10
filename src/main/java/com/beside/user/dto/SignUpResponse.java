package com.beside.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpResponse {
    private String userId;
    private String desc;
}
