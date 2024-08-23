package com.beside.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvitationResponse {
    private String invitationId;
    private String scheduleId;
    private String createUser;
    private String nickname;
    private int imgNumber;
    private String imgUrl;
    private String img;
    private LocalDateTime scheduleDate;
    private String mountainName;
    private String courseName;
    private String text;
}
