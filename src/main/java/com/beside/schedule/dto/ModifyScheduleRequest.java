package com.beside.schedule.dto;

import lombok.Data;

@Data
public class ModifyScheduleRequest {
    private String scheduleId;
    private String mountainId; //산 pk
    private int memberCount; //인원수
    private String scheduleDate; //등산날짜, yyyymmddhhmm yyyy년 mm월 dd일 hh시 mm분
    private String course; //등산코스
}
