package com.beside.schedule.dto;

import com.beside.mountain.dto.Course;
import com.beside.weather.dto.WeatherResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springdoc.webmvc.core.fn.SpringdocRouteBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class DetailScheduleResponse {
    private String scheduleId;
    private String mountainId;
    private String mountainName;
    private String courseName;
    private LocalDateTime scheduleDate;
    private int memberCount;
    private BigDecimal mountainHigh;
    private String mountainLevel;
    private String mountainAddress;
    private String mountainImg;
    private Course course; //코스정보
    private List<WeatherResponse> weatherList;
    private boolean famous100;
}
