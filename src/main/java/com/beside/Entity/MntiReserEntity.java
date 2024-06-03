package com.beside.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(callSuper=false) //lombok
@Table(name = "MOUNTAIN_RESER")
public class MntiReserEntity {

    @Id
    @Column(name = "MNTI_CNT", nullable = false)
    private int mntiCnt = 0;

    @Column(name = "id")
    private String id;

    @Column(name = "MNTILIST_NO")
    private String mntilistNo;

    @Column(name = "MNTI_CAUTION")
    private String mntiCaution;

    @Column(name = "MNTI_CLIM_TM")
    private String mntiClimTm;

    @Column(name = "MNTI_CS")
    private String mntiCs;

    @Column(name = "MNTI_CS_NAME")
    private String mntiCsName;

    @Column(name = "MNTI_DESC_TM")
    private String mntiDescTm;

    @Column(name = "MNTI_DIST")
    private String mntiDist;

    @Column(name = "MNTI_END_TM")
    private String mntiEndTm;

    @Column(name = "MNTI_FE")
    private String mntiFe;

    @Column(name = "MNTI_JSON")
    private String mntiJson;

    @Column(name = "MNTI_MT")
    private String mntiMt;

    @Column(name = "MNTI_NAME")
    private String mntiName;

    @Column(name = "MNTI_LAB")
    private String mntiReb;

    @Column(name = "MNTI_RESER")
    private LocalDate mntiReser;

    @Column(name = "MNTI_STR_DT")
    private LocalDate mntiStrDt;

    @Column(name = "MNTI_STR_TM")
    private String mntiStrTm;

    @Column(name = "MNTI_STS")
    private String mntiSts;



}
