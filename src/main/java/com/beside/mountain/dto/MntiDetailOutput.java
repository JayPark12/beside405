package com.beside.mountain.dto;

import com.beside.weather.dto.Weather;
import com.beside.weather.dto.WeatherResponse;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MntiDetailOutput {

    private String mntiName;    //산이름
    private String mntiLevel;     //산난이도
    private BigDecimal mntiHigh;    //고도
    private String mntiAddress;     //산 소제지
    private String content;      //산소개
    private List<Course> course; //코스정보
    private List<String> photoFile; //사진 정보
    private List<WeatherResponse> weatherList;  //7일까지의 데이터
}

