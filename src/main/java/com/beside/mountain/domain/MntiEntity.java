package com.beside.mountain.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "MOUNTAIN_INFO")
public class MntiEntity {

    @Id
    @Column(name = "MNTI_LIST_NO", nullable = false)
    private String mntilistNo;  //산번호

    @Column(name = "MNTI_NAME", nullable = false)
    private String mntiName;    //산이름

    @Column(name = "MNTI_CAUTION", nullable = false)
    private String mntiCaution;  //주의사항

    @Column(name = "MNTI_LEB", nullable = false)
    private String mntiLeb;

    @Column(name = "MNTI_ADD", nullable = false)
    private String mntiAdd;

    @Column(name = "MNTI_HIGH", nullable = false)
    private String mntihigh;

}
