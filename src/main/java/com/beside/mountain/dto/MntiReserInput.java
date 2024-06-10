package com.beside.mountain.dto;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MntiReserInput {
    private String jwtToken ;    //토큰값
    private String mntiname;    //산이름
    private String mntilistno ; //산코드
    private String mntiadd;     //산주소
    private String coursno;  //산코스번호
    private LocalDate mntiStrDt ;// 등산 시작 날짜


}
