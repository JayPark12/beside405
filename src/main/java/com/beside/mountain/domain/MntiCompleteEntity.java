package com.beside.mountain.domain;

import com.beside.mountain.pk.MountainResrPk;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter @Setter @ToString
@IdClass(MountainResrPk.class)
@NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(callSuper=false) //lombok
@Table(name = "MOUNTAIN_COMPLETE")
public class MntiCompleteEntity {

//    /** VARCHAR2(1)         */     @Column(name="MJ_GB"              , length=1                               ) private String      mjGb                                ;
//    /** NUMBER(16,2)        */     @Column(name="LIFT_CHG"           , precision=16, scale=2                  ) private BigDecimal liftChg           = BigDecimal.ZERO ;

    @Id
    @Column(name = "MNTI_CNT", nullable = false)
    private int mntiCnt = 0;

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Id
    @Column(name = "MNTI_LIST_NO", nullable = false)
    private String mntiListNo;

    @Column(name = "MNTI_CLIM_TM")
    private LocalTime mntiClimTm;

    @Column(name = "MNTI_NAME")
    private String mntiName;

    @Column(name = "MNTI_COURSE")
    private String mntiCourse;

    @Column(name = "MNTI_COURSE_NAME")
    private String mntiCourseName;

    @Column(name = "MNTI_STR_TM")
    private LocalTime mntiStrTm;

    @Column(name = "MNTI_END_TM")
    private LocalTime mntiEndTm;

    @Column(name = "MNTI_RESER_DATE")
    private LocalDate mntiReserDate;

    @Column(name = "MNTI_STR_DATE")
    private LocalDate mntiStrDate;

    @Column(name = "MNTI_HIGH",  precision=5, scale=2, nullable = false)
    private BigDecimal mntihigh = BigDecimal.ZERO;

    @Column(name = "MNTI_USER_FILL")
    private String mntiUserFill;

    @Column(name = "MNTI_USER_REVIEW", length = 1000)
    private String mntiUserReview;
}
