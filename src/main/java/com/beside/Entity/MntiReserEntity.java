package com.beside.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MntiReserEntity {

    @Id
    private String mntiCnt;        //등산 횟수 (같은산)
    private String id     ;
    private String mntilistNo;      //산넘버
    private String mntiCs;          //산코스 넘버
    private String mntiCsName;     //코스이름
    private String mntiName;
    private String mntiJson;       //산관련 Json 파일
    private Long   mntiDist;       //코스 거리
    private String mntiMt;         //준비물
    private String mntiCaution;    //주의사항
    private LocalDate mntiStrTm;   //등산시작시간
    private LocalDate mntiEndTm;   //등산종료시간
    private LocalDate mntiReser;   //등산예약시간 날짜
    private LocalDate mntiStrDt;   //등산 날짜
    private String   mntiSts;      //등산 상태 ex) 등산예약 4 등산 중 0 등산 완료 1 등산 실패 2
    private String   mntiReb;      //등산 난이도
    private String   mntiClimTm;   //등산 시간 (예상 소요시간) 더해서 처리했음
    private String   mntiDescTm;   //하산시간(일단 만들어만 놈)
    private String   mntiFd;

}
