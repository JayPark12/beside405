package com.beside.schedule.dto;

import com.beside.weather.dto.WeatherResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponse {
    private String scheduleId;
    private String mountain; //산 pk
    private int memberCount; //인원수
    private LocalDateTime scheduleDate; //등산날짜, yyyymmddhhmm yyyy년 mm월 dd일 hh시 mm분
    private String course; //등산코스
    private List<WeatherResponse> weatherList;
}
