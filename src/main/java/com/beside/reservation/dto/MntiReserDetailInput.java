package com.beside.reservation.dto;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MntiReserDetailInput {
    private String mntiListNo; //산코드
    private String mntiCourse;  //산코스번호
    private LocalDate mntiStrDate;// 등산 시작 날짜
}
