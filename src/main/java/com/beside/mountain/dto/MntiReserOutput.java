package com.beside.mountain.dto;

import com.beside.weather.dto.Weather;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MntiReserOutput {

    private String mntiName;    //산이름
    private String mntiListNo;  //산 넘버
    private String mntiLevel;     //산난이도
    private BigDecimal mntiHigh;    //고도
    private String mntiAdd;     //산 소제지

    private List<Course> course; //코스정보
    private List<String> potoFiles; //사진 정보
    //private List<Weather> weatherList;
}

