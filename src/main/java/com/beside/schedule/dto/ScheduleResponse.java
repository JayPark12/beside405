package com.beside.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponse {
    private String scheduleId;
    private String mountain; //산 pk
    private int memberCount; //인원수
    private LocalDateTime scheduleDate; //등산날짜, yyyymmddhhmm yyyy년 mm월 dd일 hh시 mm분
    private String course; //등산코스
}
