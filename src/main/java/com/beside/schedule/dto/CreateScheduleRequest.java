package com.beside.schedule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateScheduleRequest {
    private String mountainId; //산 pk
    private int memberCount; //인원수

    @Schema(description = "등산 날짜", example = "yyyymmddhhmm")
    private String scheduleDate; //등산날짜, yyyymmddhhmm yyyy년 mm월 dd일 hh시 mm분
    private String courseNo; //등산코스

}
