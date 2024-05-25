package com.beside.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Wather {
    private String rain_persent;
    private String rain_type;   //(단기) 없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4)
    private String sky_state;   //  맑음(1), 구름많음(3), 흐림(4)
    private String wind_power;


}
