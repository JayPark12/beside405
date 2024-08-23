package com.beside.mountain.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "MOUNTAIN_INFO")
public class MntiEntity {

    @Id
    @Column(name = "MNTI_LIST_NO", nullable = false)
    private String mntiListNo;  //산번호

    @Column(name = "MNTI_NAME", nullable = false)
    private String mntiName;    //산이름

    @Column(name = "MNTI_CAUTION", nullable = false)
    private String mntiCaution;  //주의사항

    @Column(name = "MNTI_LEVEL", nullable = false)
    private String mntiLevel;

    @Column(name = "MNTI_ADD", nullable = false)
    private String mntiAdd;

    @Column(name = "MNTI_HIGH",  precision=5, scale=2, nullable = false)
    private BigDecimal mntihigh = BigDecimal.ZERO;

    @Column(name = "famous_100")
    private boolean famous100;

    @Column(name = "seoul_trail")
    private boolean seoulTrail;

    @Column(name = "website")
    private String website;

}
