package com.beside.weather.dto;

import lombok.Data;

@Data
public class WeatherResponse {
    private String date;
    private String temperature; //TMP
    private String skyState; //SKY
    private String rainPersent; //POP
}
